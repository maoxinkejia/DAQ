package com.qcxk.service.impl;

import com.qcxk.common.RecordEnum;
import com.qcxk.controller.model.query.TerminalDeviceConfigDTO;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.dao.TerminalDeviceDao;
import com.qcxk.exception.BusinessException;
import com.qcxk.exception.ParamException;
import com.qcxk.model.VO.TerminalDataListVO;
import com.qcxk.model.device.TerminalDevice;
import com.qcxk.model.device.TerminalDeviceConfig;
import com.qcxk.service.AlarmService;
import com.qcxk.service.TerminalDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.qcxk.common.Constants.*;
import static com.qcxk.util.BusinessUtil.*;

@Slf4j
@Service
public class TerminalDeviceServiceImpl implements TerminalDeviceService {
    @Value("${upload.image.url:/home/pictures}")
    private String imageUrl;

    @Autowired
    private AlarmService alarmService;
    @Autowired
    private TerminalDeviceDao dao;

    @Override
    public TerminalDevice findByDeviceNum(String deviceNum) {
        return dao.findByDeviceNum(deviceNum);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TerminalDevice add(TerminalDevice device) {
        checkTerminalDeviceParam(device);

        checkExistsTerminalDevice(device.getDeviceNum());

        initTerminalDeviceConfig(device);
        initAlarmType(device);

        device.setSendStatus(INIT_STATUS);
        device.setWellLidOpenStatus(DISABLED);
        device.setCh4GasConcentration(0.0d);
        device.setWaterDepth(0.0d);
        device.setTemperature(0.0d);
        device.setDeviceBatVol(0.0d);
        device.setWellLidBatVol(0.0d);
        device.setCreateTime(new Date());
        device.setDelStatus(NOT_DELETED);
        device.setBootTime(null);
        device.setShutdownTime(null);
        device.setUpdateUser(null);
        device.setUpdateTime(null);
        device.setDelTime(null);

        int num = dao.addTerminalDevice(device);

        log.info("add terminalDevice success, device: {}, num: {}", device, num);
        return device;
    }

    private void checkExistsTerminalDevice(String deviceNum) {
        TerminalDevice device = findByDeviceNum(deviceNum);
        if (device != null) {
            throw new BusinessException(String.format("此设备号【%s】已经存在，请检查设备号后重新添加", deviceNum));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDevice(TerminalDevice device) {
        checkTerminalDeviceParam(device);

        device.setUpdateTime(new Date());
        dao.update(device);
    }

    @Override
    public List<TerminalDeviceConfig> findConfigByDeviceNum(String deviceNum) {
        return dao.findConfigByDeviceNum(deviceNum);
    }

    @Override
    public List<TerminalDevice> findList(TerminalDeviceDTO dto) {
        List<TerminalDevice> list = findBaseList(dto);
        for (TerminalDevice device : list) {
            List<TerminalDeviceConfig> configs = dao.findConfigByDeviceNum(device.getDeviceNum());
            buildTerminalDeviceStatus(device, configs);

            String imagePath = device.getImagePath();
            if (StringUtils.isBlank(imagePath)) {
                device.setImagePaths(Collections.emptyList());
                continue;
            }

            device.setImagePaths(Arrays.asList(imagePath.split(";")));
        }

        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBootTime(TerminalDevice device) {
        if (device.getBootTime() != null) {
            return;
        }

        device.setBootTime(new Date());
        dao.updateBootTime(device);
        log.info("device first online, bootTime: {}, deviceNum: {}", device.getBootTime(), device.getDeviceNum());
    }

    @Override
    public String uploadDeviceImages(MultipartFile[] files) {
        log.info("upload files length: {}", files.length);

        if (files.length == 0) {
            return "";
        }

        Assert.hasLength(imageUrl, "imageUrl mast not be null!!!");
        File file = new File(imageUrl);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            log.info("imageUrl doesn't exists, mkdirs: {}", mkdirs);
        }

        List<String> imagePathList = new ArrayList<>(files.length);

        for (MultipartFile pictureFile : files) {
            if (pictureFile.isEmpty()) {
                continue;
            }

            String fullPath = imageUrl + File.separator + pictureFile.getOriginalFilename();
            try {
                FileUtils.copyInputStreamToFile(pictureFile.getInputStream(), new File(fullPath));
                imagePathList.add(fullPath);
            } catch (IOException e) {
                log.error("upload picture failed, picture: {}, error: {}", pictureFile.getOriginalFilename(), e);
            }
        }


        if (CollectionUtils.isEmpty(imagePathList)) {
            return "";
        }

        return String.join(";", imagePathList);
    }

    @Override
    public List<TerminalDataListVO> findDataList(TerminalDeviceDTO dto) {
        return findBaseList(dto).stream()
                .map(terminalDevice -> buildTerminalDataList(terminalDevice, findConfigByDeviceNum(terminalDevice.getDeviceNum())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String deviceNum) {
        TerminalDevice device = dao.findByDeviceNum(deviceNum);
        device.setDelStatus(DELETED);
        device.setDelTime(new Date());
        device.setUpdateTime(new Date());

        int num = dao.update(device);

        log.info("delete terminalDevice success, num: {}", num);
    }

    @Override
    public List<TerminalDevice> findBaseList(TerminalDeviceDTO dto) {
        List<TerminalDevice> list = dao.findList(dto);
        list.forEach(device -> {
            calculateDeviceBatVolLeft(device);
            calculateWellLidBatVolLeft(device);
        });

        return list;
    }

    @Override
    public List<TerminalDeviceConfig> findConfigList(TerminalDeviceDTO dto) {
        return dao.findConfigByDeviceNum(dto.getDeviceNum());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateConfigByDeviceNum(List<TerminalDeviceConfigDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            log.info("update list is empty");
            return;
        }

        TerminalDeviceConfig config = dao.findConfigByDeviceNumValueType(list.get(0));
        Assert.notNull(config, "设备标定对象为空");

        for (TerminalDeviceConfigDTO dto : list) {

            config.setConfVal(dto.getConfVal());
            config.setChangeStatus(ENABLED);
            config.setUpdateTime(new Date());

            int num = dao.updateDeviceConfig(config);
            int num1 = dao.updateDeviceSendStatus(dto.getDeviceNum(), NOT_SEND);
            log.info("update device config success, deviceNum: {}, confName, confType: {}, num: {}, num1: {}",
                    dto.getDeviceNum(), config.getConfName(), config.getConfType(), num, num1);
        }
    }

    @Override
    public List<TerminalDeviceConfig> findChangedConfByDeviceNum(String deviceNum) {
        return dao.findChangedConfByDeviceNum(deviceNum);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateApplyTime(String deviceNum) {
        return dao.updateApplyTime(deviceNum);
    }

    @Override
    public void updateAlarmTime(String deviceNum) {
        int num = dao.updateAlarmTime(deviceNum);
        log.info("update alarmTime success, deviceNum: {}, num: {}", deviceNum, num);
    }

    /**
     * 初始化设备对应的告警参数详情
     */
    private void initTerminalDeviceConfig(TerminalDevice device) {
        RecordEnum[] values = RecordEnum.values();
        List<TerminalDeviceConfig> configs = Arrays.stream(values)
                .map(recordEnum -> buildConfig(recordEnum, device))
                .collect(Collectors.toList());

        int num = dao.batchAddTerminalDeviceConfigs(configs);
        log.info("init and batch add terminalDeviceConfigs success, num: {}", num);
    }

    private TerminalDeviceConfig buildConfig(RecordEnum recordEnum, TerminalDevice device) {
        TerminalDeviceConfig config = new TerminalDeviceConfig();
        config.setDeviceNum(device.getDeviceNum());
        config.setLocation(device.getLocation());
        config.setConfName(recordEnum.getName());
        config.setConfType(recordEnum.getType());
        config.setConfVal((Objects.equals(recordEnum, RecordEnum.WELL_LID_BAT_VOL_THRESHOLD) ? WELL_LID_BAT_VOL_THRESHOLD : null));
        config.setChangeStatus(DISABLED);
        config.setUpdateTime(null);
        config.setUpdateUser(device.getCreateUser());

        return config;
    }

    /**
     * 添加设备时初始化设备告警类型
     */
    private void initAlarmType(TerminalDevice device) {
        alarmService.addAlarmType(device.getDeviceNum());
    }

    private void checkTerminalDeviceParam(TerminalDevice device) {
        if (StringUtils.isBlank(device.getDeviceNum())) {
            throw new ParamException("设备序列号不能为空");
        }

        if (StringUtils.isBlank(device.getLocation())) {
            throw new ParamException("设备位置不能为空");
        }
    }
}

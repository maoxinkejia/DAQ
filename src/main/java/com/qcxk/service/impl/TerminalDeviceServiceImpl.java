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
import com.qcxk.service.TerminalDeviceDetailService;
import com.qcxk.service.TerminalDeviceService;
import com.qcxk.service.TerminalPicturesService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.qcxk.common.Constants.*;
import static com.qcxk.util.BusinessUtil.*;

@Slf4j
@Service
public class TerminalDeviceServiceImpl implements TerminalDeviceService {
    @Value("${upload.image.url:/home/pictures}")
    private String pictureUrl;

    @Autowired
    private TerminalDeviceDao dao;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private TerminalPicturesService terminalPicturesService;
    @Autowired
    private TerminalDeviceDetailService terminalDeviceDetailService;

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
        TerminalDevice exists = dao.findByDeviceNum(device.getDeviceNum());
        if (!Objects.equals(exists.getLocation(), device.getLocation())) {
            log.info("update device location, need to update other tables, old location: {}, new location: {}", exists.getLocation(), device.getLocation());

        }

        device.setUpdateTime(new Date());
        dao.update(device);
    }

    @Override
    public List<TerminalDeviceConfig> findConfigByDeviceNum(String deviceNum) {
        return dao.findConfigByDeviceNum(deviceNum, ALARM_TYPE);
    }

    @Override
    public List<TerminalDevice> findList(TerminalDeviceDTO dto) {
        List<TerminalDevice> list = findBaseList(dto);
        for (TerminalDevice device : list) {
            List<TerminalDeviceConfig> configs = dao.findConfigByDeviceNum(device.getDeviceNum(), ALARM_TYPE);
            buildTerminalDeviceStatus(device, configs);
        }

        return list;
    }

    @Override
    public void uploadDeviceImages(List<MultipartFile> files, String deviceNum) {
        log.info("upload files size: {}", files.size());

        mkdir(deviceNum);

        TerminalDevice terminalDevice = dao.findByDeviceNum(deviceNum);
        if (terminalDevice == null) {
            // 新设备需要全部添加
            terminalPicturesService.processNewDevicePictures(files, pictureUrl, deviceNum);
            return;
        }

        terminalPicturesService.processExistsDevicePictures(files, pictureUrl, deviceNum);
    }

    private void mkdir(String deviceNum) {
        File file = new File(pictureUrl + File.separator + deviceNum);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            log.info("pictureUrl doesn't exists, mkdirs: {}", mkdirs);
        }
    }

    @Override
    public List<TerminalDataListVO> findDataList(TerminalDeviceDTO dto) {
        List<TerminalDataListVO> list = dao.findDataList(dto);
        list.forEach(vo -> {
            buildTerminalDataList(vo, findConfigByDeviceNum(vo.getDeviceNum()));
            vo.setPictures(terminalPicturesService.findByDeviceNum(vo.getDeviceNum(), NOT_DELETED));
        });

        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(String deviceNum) {
        TerminalDevice device = dao.findByDeviceNum(deviceNum);
        device.setDelStatus(DELETED);
        device.setDelTime(new Date());
        device.setUpdateTime(new Date());

        int num = dao.update(device);
        int num1 = dao.updateConfig2Deleted(deviceNum);
        int num2 = terminalDeviceDetailService.updateDetail2Deleted(deviceNum);
        int num3 = alarmService.updateAlarmType2Deleted(deviceNum);
        int num4 = alarmService.updateAlarmDetail2Deleted(deviceNum);

        log.info("delete terminalDevice success, num: {}", num);
        log.info("delete terminalDeviceConfig success, num: {}", num1);
        log.info("delete terminalDeviceDetail success, num: {}", num2);
        log.info("delete alarmType success, num: {}", num3);
        log.info("delete alarmDetail success, num: {}", num4);
    }

    @Override
    public List<TerminalDevice> findBaseList(TerminalDeviceDTO dto) {
        List<TerminalDevice> list = dao.findList(dto);
        list.forEach(device -> {
            calculateDeviceBatVolLeft(device);
            calculateWellLidBatVolLeft(device);
            device.setPictures(terminalPicturesService.findByDeviceNum(device.getDeviceNum(), NOT_DELETED));
        });

        return list;
    }

    @Override
    public List<TerminalDeviceConfig> findConfigList(TerminalDeviceDTO dto) {
        return dao.findConfigByDeviceNum(dto.getDeviceNum(), dto.getSettingType());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateConfigByDeviceNum(List<TerminalDeviceConfigDTO> list, Integer type) {
        if (CollectionUtils.isEmpty(list)) {
            log.info("update list is empty");
            return;
        }

        TerminalDevice device = dao.findByDeviceNum(list.get(0).getDeviceNum());
        Assert.notNull(device, "设备标定对象为空");

        for (TerminalDeviceConfigDTO dto : list) {
            int num = dao.updateDeviceConfig(dto);
            log.info("update device config success, deviceNum: {}, confType: {}, num: {}", dto.getDeviceNum(), dto.getConfType(), num);
        }

        if (Objects.equals(type, ALARM_TYPE)) {
            // 告警类型不需要往设备回写，不需要更新设备状态
            return;
        }

        updateDeviceSendStatus(list.get(0).getDeviceNum(), NOT_SEND);
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDeviceSendStatus(String deviceNum, Integer sendStatus) {
        int num = dao.updateDeviceSendStatus(deviceNum, sendStatus);
        log.info("update terminalDevice sendStatus success, deviceNum: {}, sendStatus: {}, num: {}", deviceNum, sendStatus, num);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateConfigSendStatus(TerminalDeviceConfig config) {
        config.setUpdateTime(new Date());
        config.setChangeStatus(SENT);
        int num = dao.updateConfigSendStatus(config);
        log.info("update terminalDeviceConfig changeStatus success, deviceNum: {}, changeStatus: {}, num: {}", config.getDeviceNum(), config.getChangeStatus(), num);
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
        config.setConfUnit(recordEnum.getUnit());
        config.setConfVal((Objects.equals(recordEnum, RecordEnum.WELL_LID_BAT_VOL_THRESHOLD) ? WELL_LID_BAT_VOL_THRESHOLD : null));
        config.setChangeStatus(DISABLED);
        config.setUpdateTime(null);
        config.setUpdateUser(device.getCreateUser());
        config.setDelStatus(NOT_DELETED);

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

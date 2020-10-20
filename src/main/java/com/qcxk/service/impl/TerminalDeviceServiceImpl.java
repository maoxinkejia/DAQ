package com.qcxk.service.impl;

import com.qcxk.common.Constants;
import com.qcxk.common.RecordEnum;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.dao.TerminalDeviceDao;
import com.qcxk.model.TerminalDevice;
import com.qcxk.model.TerminalDeviceConfig;
import com.qcxk.model.VO.TerminalDataListVO;
import com.qcxk.service.AlarmService;
import com.qcxk.service.TerminalDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.qcxk.util.BusinessUtil.buildTerminalDataList;

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
        initTerminalDeviceConfig(device);

        device.setCreateTime(new Date());
        device.setDelStatus(Constants.NOT_DELETED);
        device.setBootTime(null);
        device.setShutdownTime(null);
        device.setUpdateUser(null);
        device.setUpdateTime(null);
        device.setDelTime(null);

        dao.insert(device);

        log.info("add terminalDevice success, device: {}", device);
        return device;
    }

    @Override
    public void updateDevice(TerminalDevice device) {
        dao.update(device);
    }

    @Override
    public List<TerminalDeviceConfig> findConfigByDeviceNum(String deviceNum) {
        return dao.findConfigByDeviceNum(deviceNum);
    }

    @Override
    public List<TerminalDevice> findList(TerminalDeviceDTO dto) {
        List<TerminalDevice> list = findBaseList(dto);
        list.forEach(device -> {
            String[] split = device.getImagePath().split(";");
            device.setImagePaths(Arrays.asList(split));
        });

        return list;
    }

    @Override
    public void updateBootTime(TerminalDevice device) {
        if (device.getBootTime() != null) {
            return;
        }

        device.setBootTime(new Date());
        dao.updateBootTime(device);
    }

    @Override
    public String uploadDeviceImages(MultipartFile[] files) {
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
                .map(terminalDevice -> buildTerminalDataList(alarmService.findAlarmListByDeviceNum(dto.getDeviceNum()), terminalDevice))
                .collect(Collectors.toList());
    }

    private List<TerminalDevice> findBaseList(TerminalDeviceDTO dto) {
        return dao.findList(dto);
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
        config.setConfName(recordEnum.getName());
        config.setConfType(recordEnum.getType());
        config.setConfVal(null);
        config.setUpdateTime(null);
        config.setUpdateUser(device.getCreateUser());

        return config;
    }
}

package com.qcxk.service.impl;

import com.qcxk.controller.model.query.AlarmDTO;
import com.qcxk.dao.DeviceAlarmDao;
import com.qcxk.model.alarm.DeviceAlarmDetail;
import com.qcxk.model.alarm.DeviceAlarmType;
import com.qcxk.service.AlarmService;
import com.qcxk.service.TerminalDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.qcxk.common.Constants.*;
import static com.qcxk.util.BusinessUtil.buildDeviceAlarmDetail;

@Slf4j
@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private DeviceAlarmDao dao;
    @Autowired
    private TerminalDeviceService terminalDeviceService;

    @Override
    public List<DeviceAlarmDetail> findAlarmListByDeviceNum(String deviceNum) {
        return dao.findAlarmListByDeviceNum(deviceNum);
    }

    @Override
    public DeviceAlarmType findDeviceAlarmType(String deviceNum) {
        return dao.findDeviceAlarmType(deviceNum);
    }

    @Override
    public void addDeviceAlarm(Map<Integer, Boolean> systemAlarm, String deviceNum, String location) {
        log.info("step into add device alarm, deviceNum: {}", deviceNum);

        for (Map.Entry<Integer, Boolean> entry : systemAlarm.entrySet()) {
            log.info("deviceAlarm key: {}, value: {}", entry.getKey(), entry.getValue());
        }

        DeviceAlarmType alarmType = findDeviceAlarmType(deviceNum);

        List<DeviceAlarmDetail> alarmList = new ArrayList<>(1);

        if (Objects.equals(alarmType.getCh4GasStatus(), ENABLED) && systemAlarm.get(CH4_CONCENTRATION_OVER_PROOF)) {
            alarmList.add(buildDeviceAlarmDetail(deviceNum, CH4_CONCENTRATION, location, CH4_CONCENTRATION_ALARM_CN));
            log.info("build device alarm, alarm type: {}, deviceNum: {}", CH4_CONCENTRATION_ALARM_CN, deviceNum);
        }

        if (Objects.equals(alarmType.getDeviceBatVolStatus(), ENABLED) && systemAlarm.get(DEVICE_BAT_VOL_LOW)) {
            alarmList.add(buildDeviceAlarmDetail(deviceNum, DEVICE_BAT_VOL, location, DEVICE_BAT_VOL_ALARM_CN));
            log.info("build device alarm, alarm type: {}, deviceNum: {}", DEVICE_BAT_VOL_ALARM_CN, deviceNum);
        }

        if (Objects.equals(alarmType.getWellLidStatus(), ENABLED) && systemAlarm.get(WELL_LID_OPENED)) {
            alarmList.add(buildDeviceAlarmDetail(deviceNum, WELL_LID_OPEN, location, WELL_LID_OPEN_ALARM_CN));
            terminalDeviceService.updateAlarmTime(deviceNum);
            log.info("build device alarm, alarm type: {}, deviceNum: {}", WELL_LID_OPEN_ALARM_CN, deviceNum);
        }

        if (Objects.equals(alarmType.getTemperatureStatus(), ENABLED) && systemAlarm.get(CH4_TEMPERATURE_OVER_PROOF)) {
            alarmList.add(buildDeviceAlarmDetail(deviceNum, TEMPERATURE, location, TEMPERATURE_ALARM_CN));
            log.info("build device alarm, alarm type: {}, deviceNum: {}", TEMPERATURE_ALARM_CN, deviceNum);
        }

        if (Objects.equals(alarmType.getWaterDepthStatus(), ENABLED) && systemAlarm.get(WATER_DEPTH_OVER_PROOF)) {
            alarmList.add(buildDeviceAlarmDetail(deviceNum, WATER_DEPTH, location, WATER_DEPTH_ALARM_CN));
            log.info("build device alarm, alarm type: {}, deviceNum: {}", WATER_DEPTH_ALARM_CN, deviceNum);
        }

        if (Objects.equals(alarmType.getWellLidBatVolStatus(), ENABLED) && systemAlarm.get(WELL_LID_BAT_VOL_LOW)) {
            alarmList.add(buildDeviceAlarmDetail(deviceNum, WELL_LID_BAT_VOL, location, WELL_LID_BAT_VOL_ALARM_CN));
            log.info("build device alarm, alarm type: {}, deviceNum: {}", WELL_LID_BAT_VOL_ALARM_CN, deviceNum);
        }

        batchAddAlarmDetails(alarmList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void batchAddAlarmDetails(List<DeviceAlarmDetail> alarmDetails) {
        if (CollectionUtils.isEmpty(alarmDetails)) {
            log.info("there is no device alarm to add");
            return;
        }

        int num = dao.batchAddAlarmDetails(alarmDetails);
        log.info("batch add deviceAlarmDetail success, num: {}", num);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addAlarmType(String deviceNum) {
        DeviceAlarmType alarmType = new DeviceAlarmType();
        alarmType.setDeviceNum(deviceNum);
        alarmType.setCh4GasStatus(DISABLED);
        alarmType.setWaterDepthStatus(DISABLED);
        alarmType.setDeviceBatVolStatus(DISABLED);
        alarmType.setWellLidBatVolStatus(DISABLED);
        alarmType.setTemperatureStatus(DISABLED);
        alarmType.setWellLidStatus(DISABLED);
        alarmType.setCreateTime(new Date());

        int num = dao.addDeviceAlarmType(alarmType);
        log.info("init device alarmType success, num: {}", num);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAlarmType(DeviceAlarmType alarmType) {
        alarmType.setUpdateTime(new Date());
        int num = dao.updateDeviceAlarmType(alarmType);
        log.info("update alarm type success, num: {}, alarmType: {}", num, alarmType);
    }

    @Override
    public List<DeviceAlarmDetail> findAlarmList(AlarmDTO dto) {
        return dao.findAlarmList(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void applyAlarm(Long id, String username) {
        DeviceAlarmDetail alarm = dao.findById(id);
        Objects.requireNonNull(alarm, "告警对象为空");

        int num = dao.updateApplyAlarm(id, username);
        int num1 = terminalDeviceService.updateApplyTime(alarm.getDeviceNum());
        log.info("deviceAlarm apply success, deviceNum: {}, id: {}, num: {}, num1: {}", alarm.getDeviceNum(), id, num, num1);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void applyByDeviceNum(String deviceNum, String username) {
        DeviceAlarmDetail alarm = dao.findWellLidOpenAlarm(deviceNum, WELL_LID_OPEN);
        if (alarm != null) {
            applyAlarm(alarm.getId(), username);
            return;
        }

        int num = terminalDeviceService.updateApplyTime(deviceNum);
        log.info("deviceAlarm apply success, deviceNum: {}, num: {}", deviceNum, num);
    }
}

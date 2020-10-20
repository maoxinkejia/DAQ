package com.qcxk.service.impl;

import com.qcxk.dao.DeviceAlarmDao;
import com.qcxk.model.DeviceAlarmDetail;
import com.qcxk.model.DeviceAlarmType;
import com.qcxk.service.AlarmService;
import com.qcxk.service.TerminalDeviceService;
import com.qcxk.util.BusinessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        DeviceAlarmType alarmType = findDeviceAlarmType(deviceNum);

        if (Objects.equals(alarmType.getCh4GasStatus(), ENABLED) && systemAlarm.get(CH4_CONCENTRATION)) {
            int num = dao.addDeviceAlarmDetail(buildDeviceAlarmDetail(deviceNum, CH4_CONCENTRATION, location, CH4_CONCENTRATION_ALARM_CN));
            log.info("add device alarm success, alarm type: {}, deviceNum: {}, num: {}", CH4_CONCENTRATION_ALARM_CN, deviceNum, num);
        }

        if (Objects.equals(alarmType.getBatVolStatus(), ENABLED) && systemAlarm.get(BAT_VOL)) {
            int num = dao.addDeviceAlarmDetail(buildDeviceAlarmDetail(deviceNum, BAT_VOL, location, BAT_VOL_ALARM_CN));
            log.info("add device alarm success, alarm type: {}, deviceNum: {}, num: {}", BAT_VOL_ALARM_CN, deviceNum, num);
        }

        if (Objects.equals(alarmType.getWellLidStatus(), ENABLED) && systemAlarm.get(WELL_LID_OPENED)) {

        }

        if (Objects.equals(alarmType.getTemperatureStatus(), ENABLED) && systemAlarm.get(TEMPERATURE)) {

        }

        if (Objects.equals(alarmType.getWaterDepthStatus(), ENABLED) && systemAlarm.get(WATER_DEPTH)) {

        }
    }
}

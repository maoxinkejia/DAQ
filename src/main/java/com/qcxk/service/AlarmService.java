package com.qcxk.service;

import com.qcxk.controller.model.query.AlarmDTO;
import com.qcxk.model.alarm.DeviceAlarmDetail;
import com.qcxk.model.alarm.DeviceAlarmType;

import java.util.List;
import java.util.Map;

public interface AlarmService {
    List<DeviceAlarmDetail> findAlarmListByDeviceNum(String deviceNum);

    DeviceAlarmType findDeviceAlarmType(String deviceNum);

    void addDeviceAlarm(Map<Integer, Boolean> systemAlarm, String deviceNum, String location);

    void addAlarmType(String deviceNum);

    void updateAlarmType(DeviceAlarmType alarmType);

    List<DeviceAlarmDetail> findAlarmList(AlarmDTO dto);

    void applyAlarm(Long id);
}

package com.qcxk.service;

import com.qcxk.model.DeviceAlarmDetail;
import com.qcxk.model.DeviceAlarmType;

import java.util.List;
import java.util.Map;

public interface AlarmService {
    List<DeviceAlarmDetail> findAlarmListByDeviceNum(String deviceNum);

    DeviceAlarmType findDeviceAlarmType(String deviceNum);

    void addDeviceAlarm(Map<Integer, Boolean> systemAlarm, String deviceNum, String location);

    void addAlarmType(String deviceNum);
}

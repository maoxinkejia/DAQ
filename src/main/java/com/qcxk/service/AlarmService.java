package com.qcxk.service;

import com.qcxk.controller.model.query.AlarmDTO;
import com.qcxk.model.alarm.DeviceAlarmDetail;
import com.qcxk.model.alarm.DeviceAlarmType;

import java.util.List;
import java.util.Map;

public interface AlarmService {
    DeviceAlarmType findDeviceAlarmType(String deviceNum);

    void addDeviceAlarm(Map<Integer, Boolean> systemAlarm, String deviceNum, String location);

    void batchAddAlarmDetails(List<DeviceAlarmDetail> alarmDetails);

    void addAlarmType(String deviceNum);

    void updateAlarmType(DeviceAlarmType alarmType);

    List<DeviceAlarmDetail> findAlarmList(AlarmDTO dto);

    void applyAlarm(Long id, String username);

    void applyByDeviceNum(String deviceNum, String username);

    int updateAlarmType2Deleted(String deviceNum);

    int updateAlarmDetail2Deleted(String deviceNum);
}

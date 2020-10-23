package com.qcxk.dao;

import com.qcxk.controller.model.query.AlarmDTO;
import com.qcxk.model.alarm.DeviceAlarmDetail;
import com.qcxk.model.alarm.DeviceAlarmType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceAlarmDao {
    List<DeviceAlarmDetail> findAlarmListByDeviceNum(String deviceNum);

    DeviceAlarmType findDeviceAlarmType(String deviceNum);

    int addDeviceAlarmDetail(DeviceAlarmDetail deviceAlarmDetail);

    int addDeviceAlarmType(DeviceAlarmType alarmType);

    int updateDeviceAlarmType(DeviceAlarmType alarmType);

    List<DeviceAlarmDetail> findAlarmList(AlarmDTO dto);

    int updateApplyAlarm(Long id);
}

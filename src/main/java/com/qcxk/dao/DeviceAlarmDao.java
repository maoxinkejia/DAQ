package com.qcxk.dao;

import com.qcxk.model.DeviceAlarmDetail;
import com.qcxk.model.DeviceAlarmType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceAlarmDao {
    List<DeviceAlarmDetail> findAlarmListByDeviceNum(String deviceNum);

    DeviceAlarmType findDeviceAlarmType(String deviceNum);

    int addDeviceAlarmDetail(DeviceAlarmDetail deviceAlarmDetail);
}

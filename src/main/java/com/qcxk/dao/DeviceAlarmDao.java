package com.qcxk.dao;

import com.qcxk.controller.model.query.AlarmDTO;
import com.qcxk.model.alarm.DeviceAlarmDetail;
import com.qcxk.model.alarm.DeviceAlarmType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DeviceAlarmDao {
    List<DeviceAlarmDetail> findAlarmListByDeviceNum(@Param("deviceNum") String deviceNum, @Param("start") Date start, @Param("end") Date end);

    DeviceAlarmType findDeviceAlarmType(String deviceNum);

    int addDeviceAlarmType(DeviceAlarmType alarmType);

    int updateDeviceAlarmType(DeviceAlarmType alarmType);

    List<DeviceAlarmDetail> findAlarmList(AlarmDTO dto);

    int updateApplyAlarm(@Param("id") Long id, @Param("applyUser") String username);

    int batchAddAlarmDetails(List<DeviceAlarmDetail> alarmList);

    DeviceAlarmDetail findById(Long id);

    DeviceAlarmDetail findWellLidOpenAlarm(@Param("deviceNum") String deviceNum, @Param("alarmType") int alarmType);

    int updateAlarmType2Deleted(String deviceNum);

    int updateAlarmDetail2Deleted(String deviceNum);
}

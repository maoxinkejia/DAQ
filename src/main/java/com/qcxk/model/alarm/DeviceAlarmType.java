package com.qcxk.model.alarm;

import lombok.Data;


@Data
public class DeviceAlarmType {
    private Long id;
    /**
     * 设备终端号
     */
    private String deviceNum;
    /**
     * 甲烷浓度告警状态  1：选择  0：未选择
     */
    private Integer ch4GasStatus;
    /**
     * 电池电量告警状态  1：选择  0：未选择
     */
    private Integer batVolStatus;
    /**
     * 井盖倾斜/移位告警状态  1：选择  0：未选择
     */
    private Integer wellLidStatus;
    /**
     * 温度告警状态   1：选择  0：未选择
     */
    private Integer temperatureStatus;
    /**
     * 液位告警状态  1：选择  0：未选择
     */
    private Integer waterDepthStatus;
}

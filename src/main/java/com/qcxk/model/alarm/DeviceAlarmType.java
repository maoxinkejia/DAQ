package com.qcxk.model.alarm;

import lombok.Data;

import java.util.Date;


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
     * 设备电池电量告警状态  1：选择  0：未选择
     */
    private Integer deviceBatVolStatus;
    /**
     * 井盖电池电量告警状态   1：选择  0：未选择
     */
    private Integer wellLidBatVolStatus;
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
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 删除状态
     */
    private Integer delStatus;
}

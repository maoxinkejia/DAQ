package com.qcxk.model.alarm;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceAlarmDetail {
    private Long id;
    /**
     * 设备序列号
     */
    private String deviceNum;
    /**
     * 位置
     */
    private String location;
    /**
     * 报警类型
     */
    private Integer alarmType;
    /**
     * 报警类型
     */
    private String alarmDescription;
    /**
     * 应答状态
     */
    private Integer applyStatus;
    /**
     * 报警时间
     */
    private Date alarmTime;
    /**
     * 应答时间
     */
    private Date applyTime;
}

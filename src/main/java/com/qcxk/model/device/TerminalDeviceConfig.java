package com.qcxk.model.device;

import lombok.Data;

import java.util.Date;

@Data
public class TerminalDeviceConfig {
    private Long id;
    /**
     * 设备号
     */
    private String deviceNum;
    /**
     * 终端位置
     */
    private String location;
    /**
     * 参数名称
     */
    private String confName;
    /**
     * 参数值
     */
    private Double confVal;
    /**
     * 参数类型
     */
    private Integer confType;
    /**
     * 参数单位
     */
    private String confUnit;
    /**
     * 改变状态 0：未改变 1：以改变
     */
    private Integer changeStatus;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 删除状态
     */
    private Integer delStatus;
}

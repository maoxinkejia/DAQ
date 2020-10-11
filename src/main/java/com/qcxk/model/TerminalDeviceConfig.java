package com.qcxk.model;

import lombok.Data;

import java.util.Date;

@Data
public class TerminalDeviceConfig {
    private Integer id;
    /**
     * 设备号
     */
    private String deviceNum;
    /**
     * 参数名称
     */
    private String confName;
    /**
     * 参数值
     */
    private Integer confVal;
    /**
     * 参数类型
     */
    private Integer confType;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateUser;
}

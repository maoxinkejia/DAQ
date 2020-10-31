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
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateUser;
}

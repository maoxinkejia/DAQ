package com.qcxk.model.message;

import lombok.Data;

import java.util.Date;

@Data
public class OriginalData {
    private Long id;
    /**
     * 设备号
     */
    private String deviceNum;
    /**
     * 原始数据
     */
    private String originalData;
    /**
     * 采集时间
     */
    private Date createTime;
}

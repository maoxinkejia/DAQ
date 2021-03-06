package com.qcxk.model.message;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Long id;
    /**
     * 设备号
     */
    private String deviceNum;
    /**
     * 功能码
     */
    private String functionNum;
    /**
     * 数据长度
     */
    private Integer dataLength;
    /**
     * 传输数据
     */
    private String data;
    /**
     * 协议头
     */
    private String prefix;
    /**
     * 协议尾
     */
    private String suffix;
    /**
     * 校验码
     */
    private String verifyCode;
    /**
     * 未经过转换的设备码 十六进制
     */
    private String deviceNumHex;
    /**
     * 原始数据
     */
    private String initialData;
    /**
     * 创建时间
     */
    private Date createTime;
}
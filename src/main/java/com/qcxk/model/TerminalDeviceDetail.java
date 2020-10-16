package com.qcxk.model;

import lombok.Data;

import java.util.Date;

@Data
public class TerminalDeviceDetail {
    private Integer id;
    private String deviceNum;
    private Integer valueType;
    private Double value;
    private Date createTime;
}

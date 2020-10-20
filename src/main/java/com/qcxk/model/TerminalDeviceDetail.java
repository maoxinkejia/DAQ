package com.qcxk.model;

import lombok.Data;

import java.util.Date;

@Data
public class TerminalDeviceDetail {
    private Long id;
    private String deviceNum;
    private Integer valueType;
    private Double value;
    private Date createTime;
}

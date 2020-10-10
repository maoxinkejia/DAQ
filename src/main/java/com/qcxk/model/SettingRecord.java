package com.qcxk.model;

import lombok.Data;

import java.util.Date;

@Data
public class SettingRecord {
    private Integer id;
    private String deviceNum;
    private String recordName;
    private Double recordVal;
    private Date updateTime;
    private String updateUser;
}

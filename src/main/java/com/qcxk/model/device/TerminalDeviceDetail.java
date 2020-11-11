package com.qcxk.model.device;

import lombok.Data;

import java.util.Date;

@Data
public class TerminalDeviceDetail {
    private Long id;
    private String deviceNum;
    /**
     * 1：甲烷浓度 2：水位深度 3：设备电池电量 4：温度 5：井盖倾斜状态 6：井盖电池电量
     */
    private Integer valueType;
    private Double value;
    private Date createTime;
    /**
     * 删除状态
     */
    private Integer delStatus;
}

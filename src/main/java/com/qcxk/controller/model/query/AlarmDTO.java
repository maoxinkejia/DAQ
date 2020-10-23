package com.qcxk.controller.model.query;

import com.qcxk.controller.model.page.PageAndPageSize;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmDTO extends PageAndPageSize {
    /**
     * 设备号
     */
    private String deviceNum;
    /**
     * 位置
     */
    private String location;
    /**
     * 告警类型
     */
    private Integer alarmType;
    /**
     * 应答状态
     */
    private Integer applyStatus;

}

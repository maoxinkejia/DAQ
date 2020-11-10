package com.qcxk.controller.model.query;

import com.qcxk.controller.model.page.PageAndPageSize;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TerminalDeviceDTO extends PageAndPageSize {
    /**
     * 设备序列号
     */
    private String deviceNum;
    /**
     * 终端位置
     */
    private String location;
    /**
     * 管井状态
     */
    private String tubeWellDescription;
    /**
     * 阀控状态 1：开启  0：关闭
     */
    private Integer controlStatus;
    /**
     * 删除标识  1：删除 0：未删除
     */
    private Integer delStatus;
    /**
     * 原始数据列表查询设备详情时
     */
    private String startDate;
    private String endDate;
    /**
     * 标定列表类型 1：标定 2：告警值
     */
    private Integer settingType;
    /**
     * 数据类型，参考TerminalDeviceDetail的valueType
     */
    private Integer valueType;
}

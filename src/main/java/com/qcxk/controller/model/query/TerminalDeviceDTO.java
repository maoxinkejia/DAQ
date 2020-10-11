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
}

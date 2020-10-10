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
}

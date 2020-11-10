package com.qcxk.controller.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalDeviceConfigDTO {
    private String deviceNum;
    private Integer confType;
    private Double confVal;
}

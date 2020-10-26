package com.qcxk.model.VO;

import com.qcxk.model.device.TerminalDeviceConfig;
import lombok.Data;

import java.util.List;

@Data
public class TerminalDeviceConfigVO {
    private String deviceNum;
    private String location;
    private List<TerminalDeviceConfig> configs;
}

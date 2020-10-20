package com.qcxk.model.VO;

import com.qcxk.model.DeviceAlarmDetail;
import com.qcxk.model.TerminalDeviceDetail;
import lombok.Data;

import java.util.List;

@Data
public class TerminalDataDetailVO {
    private List<TerminalDeviceDetail> ch4GasConcentrationList;
    private List<TerminalDeviceDetail> waterDepthList;
    private List<TerminalDeviceDetail> temperatureList;
    private List<TerminalDeviceDetail> batVolList;
    private List<DeviceAlarmDetail> alarmList;
}

package com.qcxk.model.VO;

import com.qcxk.model.alarm.DeviceAlarmDetail;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TerminalDataDetailVO {
    private TerminalDataDetailListVO ch4GasConcentrationList;
    private TerminalDataDetailListVO waterDepthList;
    private TerminalDataDetailListVO temperatureList;
    private TerminalDataDetailListVO deviceBatVolList;
    private TerminalDataDetailListVO wellLidBatVolList;
    private List<DeviceAlarmDetail> alarmList;

    @Data
    public static class TerminalDataDetailListVO {
        private List<String> date = new ArrayList<>(3);
        private List<Double> value = new ArrayList<>(3);
    }
}

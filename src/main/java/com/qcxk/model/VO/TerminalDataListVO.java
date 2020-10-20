package com.qcxk.model.VO;

import com.qcxk.model.DeviceAlarmDetail;
import lombok.Data;

import java.util.List;

@Data
public class TerminalDataListVO{
    /**
     * 设备号
     */
    private String deviceNum;
    /**
     * 位置
     */
    private String location;
    /**
     * 水位深度
     */
    private Double waterDepth;
    /**
     * 设备温度
     */
    private Integer temperature;
    /**
     * 电池电压
     */
    private Double batVol;
    /**
     * 甲烷气体浓度(LEL%)
     */
    private Double ch4GasConcentration;
    /**
     * 井盖是否掀开 1：掀开 0：未掀开
     */
    private Integer wellLidOpenStatus;
    /**
     * 告警列表信息
     */
    private List<DeviceAlarmDetail> alarmList;
}

package com.qcxk.model.VO;

import com.qcxk.model.alarm.DeviceAlarmDetail;
import lombok.Data;

import java.util.List;

@Data
public class TerminalDataListVO {
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
     * 是否达到阈值
     */
    private Integer waterDepthStatus;
    /**
     * 设备温度
     */
    private Double temperature;
    /**
     * 是否到达阈值
     */
    private Integer temperatureStatus;
    /**
     * 电池电压
     */
    private Double deviceBatVol;
    /**
     * 设备剩余电池电量  单位：%
     */
    private Double deviceBatVolLeft;
    /**
     * 是否达到阈值
     */
    private Integer deviceBatVolStatus;
    /**
     * 甲烷气体浓度(LEL%)
     */
    private Double ch4GasConcentration;
    /**
     * 是否达到阈值
     */
    private Integer ch4GasConcentrationStatus;
    /**
     * 井盖是否掀开 1：掀开 0：未掀开
     */
    private Integer wellLidOpenStatus;
    /**
     * 井盖电池电量
     */
    private Double wellLidBatVol;
    /**
     * 井盖剩余电池电量 单位：%
     */
    private Double WellLidBatVolLeft;
    /**
     * 是否达到阈值
     */
    private Integer wellLidBatVolStatus;
    /**
     * 告警列表信息
     */
    private List<DeviceAlarmDetail> alarmList;
}

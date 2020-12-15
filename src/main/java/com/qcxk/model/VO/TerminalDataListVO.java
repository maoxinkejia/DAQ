package com.qcxk.model.VO;

import com.qcxk.model.alarm.DeviceAlarmDetail;
import com.qcxk.model.device.TerminalPictures;
import lombok.Data;

import java.util.Date;
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
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 水位深度
     */
    private Double waterDepth = 0.0d;
    /**
     * 是否达到阈值
     */
    private Integer waterDepthStatus;
    /**
     * 设备温度
     */
    private Double temperature = 0.0d;
    /**
     * 是否到达阈值
     */
    private Integer temperatureStatus;
    /**
     * 电池电压
     */
    private Double deviceBatVol = 0.0d;
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
    private Double ch4GasConcentration = 0.0d;
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
    private Double wellLidBatVol = 0.0d;
    /**
     * 井盖剩余电池电量 单位：%
     */
    private Double WellLidBatVolLeft;
    /**
     * 是否达到阈值
     */
    private Integer wellLidBatVolStatus;
    /**
     * 设备状态
     * 只有所有状态都是正常时，此状态才是正常，任一状态为异常，此状态都是异常
     */
    private Integer deviceStatus;
    /**
     * 图片信息
     */
    private String imagePath;
    /**
     * 图片列表信息
     */
    private List<TerminalPictures> pictures;
    /**
     * 设备更新时间
     */
    private Date updateTime;
    /**
     * 井盖移位状态告警时间/应答时间
     */
    private Date wellLidOpenStatusAlarmTime;
    private Date wellLidOpenStatusApplyTime;
    /**
     * 告警列表信息
     */
    private List<DeviceAlarmDetail> alarmList;
}

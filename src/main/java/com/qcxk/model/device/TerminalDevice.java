package com.qcxk.model.device;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TerminalDevice {
    private Long id;
    /**
     * 终端序列号
     */
    private String deviceNum;
    /**
     * 物联网卡号
     */
    private String iotCardNum;
    /**
     * 终端IP
     */
    private String deviceIP;
    /**
     * 水位深度
     */
    private Double waterDepth;
    /**
     * 设备温度
     */
    private Double temperature;
    /**
     * 电池电压
     * 电池电压满电压是18.0V。 没有电压是10.0V。
     * 展示时需要计算百分比
     */
    private Double deviceBatVol;
    /**
     * 井盖电池电压
     * 满电压是3.6V左右，停止放电电压是2.2V
     */
    private Double wellLidBatVol;
    /**
     * 甲烷气体浓度(LEL%)
     */
    private Double ch4GasConcentration;
    /**
     * 井盖是否掀开 1：掀开 0：未掀开
     */
    private Integer wellLidOpenStatus;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 终端位置
     */
    private String location;
    /**
     * 管井状态（现场情景描述）
     */
    private String tubeWellDescription;
    /**
     * 阀控状态 枚举开关
     */
    private Integer controlStatus;
    /**
     * 管井照片地址
     */
    private String imagePath;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 标定/告警相关设置发送状态
     * 0:初始未设置  1:设置未发送  2:已发送
     */
    private Integer sendStatus;
    /**
     * 井盖应答时间
     */
    private Date applyTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 设备开机时间
     */
    private Date bootTime;
    /**
     * 设备关机时间
     */
    private Date shutdownTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 修改人
     */
    private String updateUser;
    /**
     * 标识 0：未删除 1：已删除
     */
    private Integer delStatus;
    /**
     * 删除时间
     */
    private Date delTime;

    /*==========================以下为设备/数据详情列表展示VO字段===============================*/
    /**
     * 图片地址列表
     */
    private List<String> imagePaths;
    /**
     * 设备剩余电池电量，单位%
     */
    private Double deviceBatVolLeft;
    /**
     * 井盖剩余电池电量，单位%
     */
    private Double WellLidBatVolLeft;
}

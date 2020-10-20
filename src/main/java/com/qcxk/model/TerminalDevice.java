package com.qcxk.model;

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

    private List<String> imagePaths;


    /**
     * 信号值
     */
    private Integer rssi;
    /**
     * 水位开关状态 1：启动 0：不启动
     */
    private Integer waterDepthStatus;
    /**
     * 水位深度阈值(报警值) 单位：米
     */
    private Double waterDepthThreshold;
    /**
     * 气体传感器状态 1：已连接 0：未连接
     */
    private Integer gasSensorStatus;
    /**
     * 气体浓度阈值 单位：%
     */
    private Double gasConcentrationThreshold;
    /**
     * 气体体积浓度
     */
    private Double gasConcentration;
    /**
     * 电池电压阈值 单位：V
     */
    private Double batVolThreshold;
    /**
     * 服务器ip
     */
    private String serverHost;
    /**
     * 服务器端口号
     */
    private String serverPort;
    /**
     * 电池剩余工作时间 单位：小时
     */
    private Integer batLeftWorkTime;
    /**
     * 设备软件版本号
     */
    private String version;
    /**
     * 井盖状态 1：连接  0：未连接
     */
    private Integer wellLidStatus;
    /**
     * 井盖电池电压
     */
    private Double wellLidBatVol;
    /**
     * 井盖上传数据时间
     */
    private Integer wellLidUploadTime;
    /**
     * 水位传感器状态 1：连接 0：未连接
     */
    private Integer waterSensorStatus;
    /**
     * 甲烷传感器状态 1：连接 0：未连接
     */
    private Integer ch4SensorStatus;
    /**
     * 甲烷气体体积浓度
     */
    private Double ch4GasVolumeConcentration;
    /**
     * 甲烷气体传感器状态   0：整除 10：预热中 1xx(>100)：低功耗 40：温度正常
     */
    private Integer ch4SensorEnum;
}

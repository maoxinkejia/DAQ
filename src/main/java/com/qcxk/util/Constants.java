package com.qcxk.util;

public class Constants {

    /*===============================消息功能码/消息前后缀=========================================*/
    /**
     * 设置IP、端口、APN、DNS
     */
    public static final String SETTING_DEVICE_IP_CODE = "81";
    /**
     * 设备登陆/心跳通知码
     */
    public static final String DEVICE_LOGIN_CODE = "85";
    /**
     * 上传探测器基本信息功能码
     */
    public static final String DEVICE_UPLOAD_DETAILS_CODE = "a1";
    /**
     * 上传探测器采集信息功能码
     */
    public static final String DEVICE_UPLOAD_FUNC_CODE = "a2";
    /**
     * 服务器发送给探测器配置功能码
     */
    public static final String SETTING_DEVICE_FUNC_CODE = "a3";
    /**
     * 服务器发送给探测器命令码
     */
    public static final String SETTING_DEVICE_COMMAND_CODE = "a4";
    /**
     * 探测器收取到服务器数据回传命令
     */
    public static final String DEVICE_CALL_BACK_CODE = "a6";
    /**
     * 接收消息的前缀，必须以68开头
     */
    public static final String PREFIX_START = "68";
    /**
     * 接收消息的后缀，必须以16结尾
     */
    public static final String SUFFIX_END = "16";

    /*=================================故障/异常代码标志位============================================*/
    public static final Integer CH4_SENSOR_FAILURE = 1;
    public static final Integer WATER_HEIGHT_FAILURE = 2;
    public static final Integer CH4_TEMPERATURE_OVER_PROOF = 1;
    public static final Integer CH4_CONCENTRATION_OVER_PROOF = 2;
    public static final Integer WATER_DEPTH_OVER_PROOF = 4;
    public static final Integer WELL_LID_OPENED = 8;
    public static final Integer WELL_LID_BAT_VOL_LOW = 10;
    public static final Integer SENSOR_BAT_VOL_LOW = 20;
    public static final Integer SYSTEM_UPLOAD_DATA = 40;

    public static final String IP_SEPARATOR = ".";
}

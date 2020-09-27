package com.qcxk.component.netty;

public class Contents {
    public static final String headSTR = "FEEF";
    public static final byte[] headBYTE = new byte[]{(byte) 0xEF, (byte) 0XFE};
    //上传探测器基本信息功能码
    public static final String RECEIVE_BASIC_INFO_CODE = "a1";
    //上传探测器采集信息功能码
    public static final String RECEIVE_COLL_INFO_CODE = "a2";
    //服务器发送给探测器配置功能码
    public static final String SEND_PROP_DATA_CODE = "a3";
    //服务器发送给探测器反馈信息码
    public static final String SEND_CALL_DATA_CODE = "a4";
    //服务器查询探测器配置信息
    public static final String SEND_QUERY_DATA_CODE = "a5";
    //探测器收取到服务器数据回传命令
    public static final String RECEIVE_CALL_DATA_CODE = "a6";
}

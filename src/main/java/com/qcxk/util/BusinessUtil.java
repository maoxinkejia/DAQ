package com.qcxk.util;

import com.qcxk.exception.VerifyDataException;
import com.qcxk.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static com.qcxk.util.Constants.*;

@Slf4j
public class BusinessUtil {

    /**
     * 获取数据长度
     */
    public static int getDataLength(String message) {
        return Integer.parseInt(message.substring(12, 14), 16);
    }

    /**
     * 校验数据位长度
     * 例：68040310d6a1460000040310d6000f011e01140a64140912050f25322f6f1ba91ec6000000001f90000000001f90a9ce
     * 000a05a0050005370100000300000000000000000000000000000000007d16
     * 前14位68040310d6a146，与后4位7d16为约定协议、设备号、功能、数据位等元数据信息
     * 中间为实际的数据存储位
     */
    private static boolean verifyDataLength(String message) {
        int totalLength = 18 + getDataLength(message) * 2;
        return message.length() == totalLength;
    }

    public static void verifyReceiveMsg(String message) {
        if (StringUtils.isBlank(message)) {
            throw new VerifyDataException("message is null!!!!!!!");
        }
        if (!message.startsWith(PREFIX_START)) {
            throw new VerifyDataException(String.format("message start word is not: %s, message: %s", PREFIX_START, message));
        }
        if (!message.endsWith(SUFFIX_END)) {
            throw new VerifyDataException(String.format("message end word is not: %s, message: %s", SUFFIX_END, message));
        }
        if (!verifyDataLength(message)) {
            throw new VerifyDataException(String.format("message data length is not true, please check data, message: %s", message));
        }
    }

    /**
     * 获取题头串  68
     */
    public static String getPrefix(String message) {
        return message.substring(0, 2);
    }

    /**
     * 获取八位设备号
     */
    public static String getDeviceNum(String message) {
        String deviceNumHex = getDeviceNumHex(message);
        int prefix = Integer.parseInt(deviceNumHex.substring(0, 2), 16);
        int suffix = Integer.parseInt(deviceNumHex.substring(2, 8), 16);
        return String.format("%02d-%d", prefix, suffix);
    }

    /**
     * 获取功能码 例如 85/a1/a2等等
     */
    public static String getFunctionNum(String message) {
        return message.substring(10, 12);
    }

    /**
     * 获取根据数据长度位获取真实数据 此时为16进制
     */
    public static String getDateString(String messageStr, int dataLength) {
        return messageStr.substring(14, dataLength + 14);
    }

    /**
     * 获取校验位校验码
     */
    public static String getVerifyCode(String messageStr, int dataLength) {
        return messageStr.substring(dataLength + 14, dataLength + 16);
    }

    public static String getSuffix(String messageStr, int dataLength) {
        return messageStr.substring(dataLength + 16, dataLength + 18);
    }

    /**
     * 获取登陆时电池电压
     */
    public static Integer getLoginBatVol(String data) {
        return Integer.parseInt(data.substring(12, 14), 16);
    }

    /**
     * 获取登陆时信号强度
     */
    public static Integer getLoginRSSI(String data) {
        return Integer.parseInt(data.substring(14, 16), 16);
    }

    /**
     * 构建登陆/心跳的返回码信息
     * 68 ID4 ID3 ID2 ID1 85 07 YY MM DD WW HH MM SS CRC 16
     */
    public static String buildLoginResponseMessage(Message message) {
        Date date = new Date();
        return PREFIX_START
                + message.getDeviceNumHex()
                + message.getFunctionNum()
                + "07"
                + DateUtils.getDateHex(date)
                + DateUtils.getWeekHex(date)
                + DateUtils.getMinuteHex(date)
                + message.getVerifyCode()
                + SUFFIX_END;
    }

    /**
     * 获取设备码 16进制
     */
    public static String getDeviceNumHex(String message) {
        return message.substring(2, 10);
    }

    /**
     * 获取水位开关状态 1：启动 0：未启动
     */
    public static Integer getWaterDepthStatus(String data) {
        return Integer.parseInt(data.substring(16, 18), 16);
    }

    /**
     * 获取水位深度阈值 单位：米
     */
    public static Double getWaterDepthThreshold(String data) {
        double value = Integer.valueOf(data.substring(18, 20), 16).doubleValue();
        return value / 100;
    }

    /**
     * 获取气体传感器状态 1：连接 0：未连接
     */
    public static Integer getGasSensorStatus(String data) {
        return Integer.parseInt(data.substring(20, 22));
    }

    /**
     * 气体浓度阈值 单位：%
     * 例：返回30   即30%
     */
    public static Double getGasConcentrationThreshold(String data) {
        return Integer.valueOf(data.substring(22, 24), 16).doubleValue();
    }

    /**
     * 气体体积浓度  单位：%
     */
    public static Double getGasConcentration(String data) {
        return Integer.valueOf(data.substring(24, 26), 16).doubleValue();
    }

    /**
     * 电池电压报警阈值  单位：V  解析完需要/10
     */
    public static Double getBatVolThreshold(String data) {
        return Integer.valueOf(data.substring(26, 28), 16).doubleValue() / 10;
    }

    /**
     * 获取服务器ip
     */
    public static String getServerHost(String data) {
        int ip1 = Integer.parseInt(data.substring(42, 44), 16);
        int ip2 = Integer.parseInt(data.substring(44, 46), 16);
        int ip3 = Integer.parseInt(data.substring(46, 48), 16);
        int ip4 = Integer.parseInt(data.substring(48, 50), 16);
        return ip1 + IP_SEPARATOR + ip2 + IP_SEPARATOR + ip3 + IP_SEPARATOR + ip4;
    }

    /**
     * 获取服务器端口号
     */
    public static String getServerPort(String data) {
        int host1 = Integer.parseInt(data.substring(50, 52), 16);
        int host2 = Integer.parseInt(data.substring(52, 54), 16);
        return Integer.toString(host1) + host2;
    }

    /**
     * 电池剩余工作时间 单位：小时
     */
    public static Integer getBatLeftWorkTime(String data) {
        int num1 = Integer.parseInt(data.substring(78, 80), 16);
        int num2 = Integer.parseInt(data.substring(80, 82), 16);
        return Integer.parseInt(Integer.toString(num1) + num2);
    }
}

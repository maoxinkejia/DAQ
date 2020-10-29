package com.qcxk.util;

import com.qcxk.model.VO.TerminalDataListVO;
import com.qcxk.model.alarm.DeviceAlarmDetail;
import com.qcxk.model.device.TerminalDevice;
import com.qcxk.model.device.TerminalDeviceDetail;
import com.qcxk.model.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.qcxk.common.Constants.*;
import static java.nio.charset.StandardCharsets.UTF_8;

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
        String message2 = getNextMessage(message);
        if (StringUtils.isBlank(message2)) {
            return true;
        }

        return verifyReceiveMsg(message2);
    }

    public static String getNextMessage(String message) {
        return message.substring(getTotalLength(message));
    }

    private static int getTotalLength(String message) {
        return 18 + getDataLength(message) * 2;
    }

    public static boolean verifyReceiveMsg(String message) {
        if (StringUtils.isBlank(message)) {
            log.info("message verify failed................. error: message is null!!!!!!!");
            return false;
        }
        if (!message.startsWith(PREFIX_START)) {
            log.info(String.format("message verify failed................. error: message start word is not: %s, message: %s", PREFIX_START, message));
            return false;
        }
        if (!message.endsWith(SUFFIX_END)) {
            log.info(String.format("message verify failed................. error: message end word is not: %s, message: %s", SUFFIX_END, message));
            return false;
        }
        if (!verifyDataLength(message)) {
            log.info(String.format("message verify failed................. error: message data length is not true, please check data, message: %s", message));
            return false;
        }

        return true;
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

        String suffixStr = deviceNumHex.substring(2, 8);
        if (Objects.equals("000000", suffixStr)) {
            return String.format("%02d-%s", prefix, suffixStr);
        }

        int suffix = Integer.parseInt(suffixStr, 16);
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

    /**
     * 获取软件版本
     */
    public static String getVersion(String data) {
        int num1 = Integer.parseInt(data.substring(14, 16), 16);
        int num2 = Integer.parseInt(data.substring(16, 18), 16);
        return "V" + num1 + "." + num2;
    }

    /**
     * 获取井盖状态 1：连接 0：未连接
     */
    public static Integer getWellLidStatus(String data) {
        return Integer.parseInt(data.substring(18, 20), 16);
    }

    /**
     * 获取井盖是否掀开状态 1：掀开 0：为掀开
     */
    public static Integer getWellLidOpenStatus(String data) {
        return Integer.parseInt(data.substring(20, 22), 16);
    }

    /**
     * 获取井盖电池电压，得到值后需要除以10，并保留1位小数  单位：V
     */
    public static Double getWellLidBatVol(String data) {
        int batVol = Integer.parseInt(data.substring(22, 24), 16);
        return getDoubleValue(batVol);
    }

    /**
     * 获取井盖上传数据时间间隔 单位：分钟
     */
    public static Integer getWellLidUploadTime(String data) {
        return Integer.parseInt(data.substring(24, 26), 16);
    }

    /**
     * 获取硬件故障代码
     */
    public static Map<Integer, Boolean> getHardwareErrorCode(String data) {
        int errorCode = Integer.parseInt(data.substring(32, 34), 16);
        Map<Integer, Boolean> hardwareFailure = new HashMap<>(2);
        hardwareFailure.put(CH4_SENSOR_FAILURE, (errorCode & CH4_SENSOR_FAILURE) == CH4_SENSOR_FAILURE);
        hardwareFailure.put(WATER_HEIGHT_FAILURE, (errorCode & WATER_HEIGHT_FAILURE) == WATER_HEIGHT_FAILURE);

        return hardwareFailure;
    }

    /**
     * 获取水位传感器在线状态  1：连接  0：未连接
     */
    public static Integer getWaterSensorStatus(String data) {
        return Integer.parseInt(data.substring(34, 36), 16);
    }

    /**
     * 获取水位深度，需要除以10，结果保留一位小数 单位：米
     */
    public static Double getWaterDepth(String data) {
        int waterDepth = Integer.parseInt(data.substring(36, 38), 16);
        return getDoubleValue(waterDepth);
    }

    /**
     * 获取甲烷气体传感器在线状态  1：连接  0：未连接
     */
    public static Integer getCH4GasSensorStatus(String data) {
        return Integer.parseInt(data.substring(38, 40), 16);
    }

    /**
     * 获取甲烷气体浓度 取值为0-1000，获取后需要除以10，以百分比展示  单位：%
     */
    public static Double getCH4GasConcentration(String data) {
        int ch4Concentration = Integer.parseInt(data.substring(40, 44), 16);
        return getDoubleValue(ch4Concentration);
    }

    /**
     * 获取甲烷气体体积浓度，取值0-100，获取后需要除以10，以百分比展示 单位：%
     */
    public static Double getCH4GasVolumeConcentration(String data) {
        int ch4VolumeConcentration = Integer.parseInt(data.substring(44, 46), 16);
        return getDoubleValue(ch4VolumeConcentration);
    }

    /**
     * 获取甲烷气体传感器状态
     */
    public static Integer getCH4SensorEnum(String data) {
        return Integer.parseInt(data.substring(46, 48), 16);
    }

    /**
     * 获取甲烷气体传感器内部温度
     */
    public static Integer getCH4GasTemperature(String data) {
        return Integer.parseInt(data.substring(48, 50), 16);
    }

    /**
     * 获取A2功能码内的设备电池电量
     */
    public static Double getDeviceBatVol(String data) {
        int deviceBatVol = Integer.parseInt(data.substring(50, 52), 16);
        return getDoubleValue(deviceBatVol);
    }

    public static Map<Integer, Boolean> getSystemErrorCode(String data) {
        int errorCode = Integer.parseInt(data.substring(66, 70), 16);
        Map<Integer, Boolean> systemFailure = new HashMap<>(7);
        putProperty(systemFailure, CH4_TEMPERATURE_OVER_PROOF, errorCode);
        putProperty(systemFailure, CH4_CONCENTRATION_OVER_PROOF, errorCode);
        putProperty(systemFailure, WATER_DEPTH_OVER_PROOF, errorCode);
        putProperty(systemFailure, WELL_LID_OPENED, errorCode);
        putProperty(systemFailure, WELL_LID_BAT_VOL_LOW, errorCode);
        putProperty(systemFailure, SENSOR_BAT_VOL_LOW, errorCode);
        putProperty(systemFailure, SYSTEM_UPLOAD_DATA, errorCode);

        return systemFailure;
    }

    private static void putProperty(Map<Integer, Boolean> map, Integer type, int errorCode) {
        map.put(type, (errorCode & type) == type);
    }

    /**
     * 获取信号值
     */
    public static Integer getRSSI(String data) {
        return Integer.parseInt(data.substring(94, 96), 16);
    }

    /**
     * 构建服务器响应消息，此消息标明此次数据正确有效
     */
    public static String buildReceiveRightDataMessage(Message message) {
        return PREFIX_START
                + message.getDeviceNumHex()
                + SETTING_DEVICE_COMMAND_CODE
                + "040404"
                + message.getDataLength()
                + message.getFunctionNum()
                + message.getVerifyCode()
                + SUFFIX_END;
    }

    /**
     * 构建服务器发送给探测器功能配置的消息体
     */
    public static String buildServerSend2DeviceMessage(Message message, TerminalDevice device) {
        StringBuffer buffer = new StringBuffer();


        return PREFIX_START +
                message.getDeviceNumHex() +
                SETTING_DEVICE_FUNC_CODE +
                "46" +
                buffer.toString() +
                message.getVerifyCode() +
                SUFFIX_END;
    }

    /**
     * 构建恢复出厂设置命令
     */
    public static String buildFactoryDataReset(Message message) {
        return PREFIX_START
                + message.getDeviceNumHex()
                + SETTING_DEVICE_COMMAND_CODE
                + "020101"
                + message.getVerifyCode()
                + SUFFIX_END;
    }

    /**
     * 构建服务器无数据时发送的消息体
     */
    public static String buildNoDataMessage(Message message) {
        return PREFIX_START
                + message.getDeviceNumHex()
                + DEVICE_RECEIVE_SERVER_CODE
                + "00"
                + message.getVerifyCode()
                + SUFFIX_END;
    }

    /**
     * 构建修改IP消息体
     */
    public static String buildChangeIpPortMessage(Message message, String ip, String port) {
        StringBuilder builder = new StringBuilder("00");

        for (String s1 : ip.split("\\.")) {
            builder.append(Integer.toHexString(Integer.parseInt(s1)));
        }
        builder.append(Integer.toHexString(Integer.parseInt(port)));

        int totalLength = Integer.parseInt("3d", 16) * 2;
        int currLength = builder.toString().length();
        for (int i = 0; i < totalLength - currLength; i++) {
            builder.append("0");
        }

        return PREFIX_START
                + message.getDeviceNumHex()
                + SETTING_DEVICE_IP_CODE
                + "3d"
                + builder.toString()
                + message.getVerifyCode()
                + SUFFIX_END;
    }

    /**
     * 构建设备基础信息
     */
    public static TerminalDevice buildTerminalDevice(Message message) {
        TerminalDevice device = new TerminalDevice();
        device.setDeviceNum(message.getDeviceNum());
        device.setLocation("暂无");
        device.setCreateUser(SYSTEM_USER);
        return device;
    }

    public static TerminalDeviceDetail buildTerminalDeviceDetail(TerminalDevice device, Integer type) {
        TerminalDeviceDetail detail = new TerminalDeviceDetail();
        detail.setDeviceNum(device.getDeviceNum());
        detail.setValueType(type);

        Double value;
        switch (type) {
            case CH4_CONCENTRATION:
                value = device.getCh4GasConcentration();
                break;
            case WATER_DEPTH:
                value = device.getWaterDepth();
                break;
            case TEMPERATURE:
                value = device.getTemperature().doubleValue();
                break;
            case DEVICE_BAT_VOL:
                value = device.getDeviceBatVol();
                break;
            default:
                throw new RuntimeException("类型异常");
        }

        detail.setValue(value);
        detail.setCreateTime(new Date());

        return detail;
    }

    /**
     * 构建数据列表数据
     */
    public static TerminalDataListVO buildTerminalDataList(List<DeviceAlarmDetail> alarms, TerminalDevice device) {
        TerminalDataListVO vo = new TerminalDataListVO();
        vo.setDeviceNum(device.getDeviceNum());
        vo.setCh4GasConcentration(device.getCh4GasConcentration());
        vo.setWaterDepth(device.getWaterDepth());
        vo.setTemperature(device.getTemperature());
        vo.setDeviceBatVol(device.getDeviceBatVol());
        vo.setLocation(device.getLocation());
        vo.setAlarmList(alarms);

        return vo;
    }

    public static DeviceAlarmDetail buildDeviceAlarmDetail(String deviceNum, int alarmType, String location, String alarmDescription) {
        DeviceAlarmDetail detail = new DeviceAlarmDetail();
        detail.setDeviceNum(deviceNum);
        detail.setLocation(location);
        detail.setAlarmType(alarmType);
        detail.setAlarmDescription(alarmDescription);
        detail.setAlarmTime(new Date());
        detail.setApplyStatus(DISABLED);
        detail.setApplyTime(null);

        return detail;
    }

    /**
     * 密码进行MD5加密
     */
    public static String getMD5Str(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(UTF_8));
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            log.error("md5 str error, str: {}", str);
            return str;
        }
    }

    public static String getCookie() {
        String s = "a59cf3b4-de8b-438f-9c74-264b6a1f3e21";
        return getMD5Str(s);
    }

    /**
     * 根据int值，除以10，取1位小数
     */
    private static double getDoubleValue(int intVal) {
        return new BigDecimal(intVal).divide(new BigDecimal(10), 1, BigDecimal.ROUND_DOWN).doubleValue();
    }
}
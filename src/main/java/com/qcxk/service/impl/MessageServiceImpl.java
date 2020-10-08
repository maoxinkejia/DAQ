package com.qcxk.service.impl;

import com.qcxk.dao.MessageDao;
import com.qcxk.model.Message;
import com.qcxk.model.TerminalDevice;
import com.qcxk.service.MessageService;
import com.qcxk.service.TerminalDeviceService;
import com.qcxk.util.BusinessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.qcxk.util.BusinessUtil.*;
import static com.qcxk.util.Constants.*;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private TerminalDeviceService terminalDeviceService;

    @Override
    public void processMsg(Message message) throws Exception {
        TerminalDevice device = terminalDeviceService.findByDeviceNum(message.getDeviceNum());
        if (device == null) {
//             todo 当前device为null，需要构建填充
            device = terminalDeviceService.add(device);
        }

        String data = message.getData();
        switch (message.getFunctionNum()) {
            case DEVICE_LOGIN_CODE:
                log.info("receive device login code: 85, terminalDevice onLine, deviceNum: {}, data: {}",
                        message.getDeviceNum(), data);
                terminalDeviceService.updateBatVolAndRssi(getLoginBatVol(data), getLoginRSSI(data), device.getId());
                break;
            case DEVICE_UPLOAD_DETAILS_CODE:
                log.info("receive device details code: A1, data: {}", data);
                buildDeviceUploadDetails(device, data);
                terminalDeviceService.updateDevice(device);
                break;
            case DEVICE_UPLOAD_FUNC_CODE:
                log.info("receive device function code: A2, data: {}", data);
                buildDeviceUploadFunction(device, data);
                terminalDeviceService.updateDevice(device);
                break;
            case DEVICE_CALL_BACK_CODE:

                break;
            default:
                log.info("receive device message, but not resolve, functionNum: {}", message.getFunctionNum());
        }
    }

    @Override
    public Message parse2Msg(String messageStr) {
        Message message = new Message();
        message.setPrefix(getPrefix(messageStr));
        message.setDeviceNum(getDeviceNum(messageStr));
        message.setFunctionNum(getFunctionNum(messageStr));
        message.setDataLength(getDataLength(messageStr));
        message.setDeviceNumHex(getDeviceNumHex(messageStr));

        int length = message.getDataLength() * 2;
        message.setData(getDateString(messageStr, length));
        message.setVerifyCode(getVerifyCode(messageStr, length));
        message.setSuffix(getSuffix(messageStr, length));
        return message;
    }

    @Override
    public List<String> responseMessage(Message message) {
        List<String> response = new ArrayList<>(1);

        switch (message.getFunctionNum()) {
            case DEVICE_LOGIN_CODE:
                response.add(BusinessUtil.buildLoginResponseMessage(message));
                break;

        }
        return response;
    }

    /**
     * 解析设备上传探测器基本信息（A1），并构建填充设备对象
     */
    private void buildDeviceUploadDetails(TerminalDevice device, String data) {
        Integer waterDepthStatus = BusinessUtil.getWaterDepthStatus(data);
        Double waterDepthThreshold = BusinessUtil.getWaterDepthThreshold(data);
        Integer gasSensorStatus = BusinessUtil.getGasSensorStatus(data);
        Double gasConcentrationThreshold = BusinessUtil.getGasConcentrationThreshold(data);
        Double gasConcentration = BusinessUtil.getGasConcentration(data);
        Double batVolThreshold = BusinessUtil.getBatVolThreshold(data);
        String serverHost = BusinessUtil.getServerHost(data);
        String serverPort = BusinessUtil.getServerPort(data);
        Integer batLeftWorkTime = BusinessUtil.getBatLeftWorkTime(data);

        device.setWaterDepthStatus(waterDepthStatus);
        device.setWaterDepthThreshold(waterDepthThreshold);
        device.setGasSensorStatus(gasSensorStatus);
        device.setGasConcentration(gasConcentration);
        device.setGasConcentrationThreshold(gasConcentrationThreshold);
        device.setBatVolThreshold(batVolThreshold);
        device.setServerHost(serverHost);
        device.setServerPort(serverPort);
        device.setBatLeftWorkTime(batLeftWorkTime);
    }

    /**
     * 解析设备上传的采集信息功能码（A2），并构建填充设备对象
     */
    private void buildDeviceUploadFunction(TerminalDevice device, String data) {
        String version = BusinessUtil.getVersion(data);
        Integer wellLidStatus = BusinessUtil.getWellLidStatus(data);
        Integer wellLidOpenStatus = BusinessUtil.getWellLidOpenStatus(data);
        Double wellLidBatVol = BusinessUtil.getWellLidBatVol(data);
        Integer wellLidUploadTime = BusinessUtil.getWellLidUploadTime(data);
        Map<Integer, Boolean> hardwareFailure = BusinessUtil.getHardwareErrorCode(data);
        Integer waterSensorStatus = BusinessUtil.getWaterSensorStatus(data);
        Double waterDepth = BusinessUtil.getWaterDepth(data);
        Integer ch4SensorStatus = BusinessUtil.getCH4SensorStatus(data);
        Double ch4GasConcentration = BusinessUtil.getCH4GasConcentration(data);
        Double ch4GasVolumeConcentration = BusinessUtil.getCH4GasVolumeConcentration(data);
        Integer ch4SensorEnum = BusinessUtil.getCH4SensorEnum(data);
        Map<Integer, Boolean> systemFailure = BusinessUtil.getSystemErrorCode(data);
        Integer rssi = BusinessUtil.getRSSI(data);

        device.setVersion(version);
        device.setWellLidStatus(wellLidStatus);
        device.setWellLidOpenStatus(wellLidOpenStatus);
        device.setWellLidBatVol(wellLidBatVol);
        device.setWellLidUploadTime(wellLidUploadTime);
        device.setWaterSensorStatus(waterSensorStatus);
        device.setWaterDepth(waterDepth);
        device.setCh4SensorStatus(ch4SensorStatus);
        device.setCh4GasConcentration(ch4GasConcentration);
        device.setCh4GasVolumeConcentration(ch4GasVolumeConcentration);
        device.setCh4SensorEnum(ch4SensorEnum);
        device.setRssi(rssi);
    }

    public static void main(String[] args) throws Exception {
        String s1 = "68040310d6a1460000040310d6000f011e01140a64140912050f25322f6f1ba91ec6000000001f90000000001f90a9ce000a05a0050005370100000300000000000000000000000000000000007d16";
        String s2 = "68040310d685091409120510060eb1001416";
        String s3 = "68040310d6a23200000000000000010000000005000000030100010000000018b1ac44000000000000000000000000000000000000000000001616";

        Integer a = 3;
        boolean b1 = (a & 1) == 1;
        boolean b2 = (a & 2) == 2;
        System.out.println(b1);
        System.out.println(b2);

        MessageServiceImpl messageService = new MessageServiceImpl();
        Message message = messageService.parse2Msg(s3);
        messageService.processMsg(message);


    }
}

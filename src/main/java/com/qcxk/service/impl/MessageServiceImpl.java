package com.qcxk.service.impl;

import com.qcxk.dao.MessageDao;
import com.qcxk.model.Message;
import com.qcxk.model.TerminalDevice;
import com.qcxk.model.TerminalDeviceDetail;
import com.qcxk.service.AlarmService;
import com.qcxk.service.MessageService;
import com.qcxk.service.TerminalDeviceDetailService;
import com.qcxk.service.TerminalDeviceService;
import com.qcxk.util.BusinessUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.qcxk.common.Constants.*;
import static com.qcxk.util.BusinessUtil.*;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao dao;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private TerminalDeviceService terminalDeviceService;
    @Autowired
    private TerminalDeviceDetailService terminalDeviceDetailService;

    @Override
    public void processMsg(List<Message> messages) {
        for (Message message : messages) {
            log.info("start process message, deviceNum: {}", message.getDeviceNum());

            TerminalDevice device = terminalDeviceService.findByDeviceNum(message.getDeviceNum());
            if (device == null) {
                device = terminalDeviceService.add(BusinessUtil.buildTerminalDevice(message));
            }

            terminalDeviceService.updateBootTime(device);

            String data = message.getData();
            switch (message.getFunctionNum()) {
                case DEVICE_LOGIN_CODE:
                    log.info("receive device login code: 85, terminalDevice onLine, data: {}", data);
                    buildDeviceLoginCode(device, data);
                    break;
                case DEVICE_UPLOAD_DETAILS_CODE:
                    log.info("receive device details code: A1, data: {}", data);
                    buildDeviceUploadDetails(device, data);
                    terminalDeviceService.updateDevice(device);
                    break;
                case DEVICE_UPLOAD_FUNC_CODE:
                    log.info("receive device function code: A2, data: {}", data);
                    buildDeviceUploadFunction(device, data);
                    break;
                case DEVICE_CALL_BACK_CODE:

                    break;
                case DEVICE_RECEIVE_SERVER_CODE:
                    log.info("receive device code: A7");
                    break;
                default:
                    log.info("receive device message, but not resolve, functionNum: {}", message.getFunctionNum());
            }
        }
    }

    @Override
    public List<Message> parse2Msg(String messageStr, List<Message> messages) {
        Message message = new Message();
        message.setInitialData(messageStr);
        message.setPrefix(getPrefix(messageStr));
        message.setDeviceNum(getDeviceNum(messageStr));
        message.setFunctionNum(getFunctionNum(messageStr));
        message.setDataLength(getDataLength(messageStr));
        message.setDeviceNumHex(getDeviceNumHex(messageStr));

        int length = message.getDataLength() * 2;
        message.setVerifyCode(getVerifyCode(messageStr, length));
        message.setSuffix(getSuffix(messageStr, length));
        message.setCreateTime(new Date());

        if (Objects.equals(message.getFunctionNum(), DEVICE_RECEIVE_SERVER_CODE)) {
            message.setData("");
        } else {
            message.setData(getDateString(messageStr, length));
        }

        messages.add(message);
        log.info("add message to list, deviceNum: {}, message: {}", message.getDeviceNum(), message.getInitialData());


        String messageStr2 = getNextMessage(messageStr);
        if (StringUtils.isBlank(messageStr2)) {
            int num = dao.addBatch(messages);

            log.info("batch add messages to db success, add num: {}", num);
            return messages;
        }

        return parse2Msg(messageStr2, messages);
    }

    @Override
    public List<String> responseMessage(List<Message> messages) {
        List<String> response = new ArrayList<>(1);

        for (Message message : messages) {
            switch (message.getFunctionNum()) {
                case DEVICE_LOGIN_CODE:
                    response.add(BusinessUtil.buildLoginResponseMessage(message));
                    break;
                case DEVICE_UPLOAD_DETAILS_CODE:
                case DEVICE_UPLOAD_FUNC_CODE:
                    response.add(BusinessUtil.buildReceiveRightDataMessage(message));
                    break;
                case DEVICE_RECEIVE_SERVER_CODE:
                    // todo 将所有需要发送的消息查询出来统一进行编辑发送
                    break;
            }

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
        Integer ch4GasSensorStatus = BusinessUtil.getCH4GasSensorStatus(data);
        Double ch4GasConcentration = BusinessUtil.getCH4GasConcentration(data);
        Double ch4GasVolumeConcentration = BusinessUtil.getCH4GasVolumeConcentration(data);
        Integer ch4SensorEnum = BusinessUtil.getCH4SensorEnum(data);
        Integer ch4GasSensorTemperature = BusinessUtil.getCH4GasTemperature(data);
        Map<Integer, Boolean> systemAlarm = BusinessUtil.getSystemErrorCode(data);
        Integer rssi = BusinessUtil.getRSSI(data);


        alarmService.addDeviceAlarm(systemAlarm, device.getDeviceNum(), device.getLocation());

        List<TerminalDeviceDetail> list = new ArrayList<>();

        if (Objects.equals(ENABLED, waterSensorStatus)) {
            device.setWaterDepth(waterDepth);
            list.add(buildTerminalDeviceDetail(device, WATER_DEPTH));
        }

        if (Objects.equals(ENABLED, ch4GasSensorStatus)) {
            device.setTemperature(ch4GasSensorTemperature);
            device.setCh4GasConcentration(ch4GasConcentration);

            list.add(buildTerminalDeviceDetail(device, CH4_CONCENTRATION));
            list.add(buildTerminalDeviceDetail(device, TEMPERATURE));
        }

        if (Objects.equals(ENABLED, wellLidStatus)) {
            device.setWellLidOpenStatus(wellLidOpenStatus);
        }

        device.setUpdateTime(new Date());
        device.setUpdateUser(SYSTEM_USER);
        terminalDeviceService.updateDevice(device);

        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        terminalDeviceDetailService.batchAddDetail(list);
    }

    /**
     * 构建设备登录时数据
     */
    private void buildDeviceLoginCode(TerminalDevice device, String data) {
        device.setBatVol(getLoginBatVol(data).doubleValue());
        device.setUpdateTime(new Date());
        device.setUpdateUser(SYSTEM_USER);

        terminalDeviceService.updateDevice(device);
        terminalDeviceDetailService.batchAddDetail(Collections.singletonList(buildTerminalDeviceDetail(device, BAT_VOL)));
    }

//    public static void main(String[] args) {
//        String s1 = "68040310d6a1460000040310d6000f011e01140a64140912050f25322f6f1ba91ec6000000001f90000000001f90a9ce000a05a0050005370100000300000000000000000000000000000000007d16";
//        String s2 = "68040310d685091409120510060eb1001416";
//        String s3 = "68040310d6a23200000000000000010000000005000000030100010000000018b1ac44000000000000000000000000000000000000000000001616";
//        String s4 = "6804000000a2320000000000000001000000000500000003010000000000000000001400000000000000000000000000000000000000000000a21668040000008509140a0e030a2e015e0040166804000000a146000004000000010f011e003c1e78140a0e030a2e02000000001f90000000001f90000000001f900014000a0003320005370100000300000000000000000000000000000000009f16";
//
//        MessageServiceImpl service = new MessageServiceImpl();
//        List<Message> messages = service.parse2Msg(s4, new ArrayList<>());
//        System.out.println(messages);
//
//    }
}

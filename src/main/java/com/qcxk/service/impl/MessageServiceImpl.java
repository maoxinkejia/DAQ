package com.qcxk.service.impl;

import com.qcxk.common.RecordEnum;
import com.qcxk.controller.model.query.TerminalDeviceConfigDTO;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.dao.MessageDao;
import com.qcxk.model.alarm.DeviceAlarmType;
import com.qcxk.model.device.TerminalDevice;
import com.qcxk.model.device.TerminalDeviceConfig;
import com.qcxk.model.device.TerminalDeviceDetail;
import com.qcxk.model.message.Message;
import com.qcxk.model.message.OriginalData;
import com.qcxk.service.AlarmService;
import com.qcxk.service.MessageService;
import com.qcxk.service.TerminalDeviceDetailService;
import com.qcxk.service.TerminalDeviceService;
import com.qcxk.util.BusinessUtil;
import com.qcxk.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
                device = terminalDeviceService.add(buildNewTerminalDevice(message));
            }

            String data = message.getData();
            switch (message.getFunctionNum()) {
                case DEVICE_LOGIN_CODE:
                    log.info("receive device login code: 85, terminalDevice onLine, data: {}", data);
                    buildDeviceLoginCode(device, data);
                    break;
                case DEVICE_UPLOAD_DETAILS_CODE:
                    log.info("receive device details code: A1, data: {}", data);
                    buildDeviceUploadDetails(device, data);
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
            return messages;
        }

        return parse2Msg(messageStr2, messages);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
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
                    buildDeviceFuncNum2Message(message, response);
                    break;
            }
        }

        return response;
    }

    private void buildDeviceFuncNum2Message(Message message, List<String> response) {
        TerminalDevice terminalDevice = terminalDeviceService.findByDeviceNum(message.getDeviceNum());
        if (!Objects.equals(NOT_SEND, terminalDevice.getSendStatus())) {
            log.info("there is no message to send, device sendStatus: {}, deviceNum: {}", terminalDevice.getSendStatus(), message.getDeviceNum());
            return;
        }

        List<TerminalDeviceConfig> configs = terminalDeviceService.findChangedConfByDeviceNum(terminalDevice.getDeviceNum());
        if (CollectionUtils.isEmpty(configs)) {
            log.info("there is no configs to sendBack to terminalDevice, deviceNum: {}", terminalDevice.getDeviceNum());
            return;
        }

        StringBuilder builder = new StringBuilder(String.format("%0140d", 0));
        log.info("need to write to device configs size: {}", configs.size());

        for (TerminalDeviceConfig config : configs) {
            buildConfig2String(config, builder);
            terminalDeviceService.updateConfigSendStatus(config);
            log.info("update terminalDeviceConfig status sent success, configId: {}", config.getId());
        }

        Date now = new Date();
        builder.replace(28, 34, DateUtils.getDateHex(now));
        builder.replace(34, 36, DateUtils.getWeekHex(now));
        builder.replace(36, 42, DateUtils.getMinuteHex(now));

        response.add(buildServerSend2DeviceMessage(message, builder));

        terminalDeviceService.updateDeviceSendStatus(terminalDevice.getDeviceNum(), SENT);
    }

    private void buildConfig2String(TerminalDeviceConfig config, StringBuilder builder) {
        switch (Objects.requireNonNull(RecordEnum.of(config.getConfType()))) {
            case CH4_GAS_VOLUME_ALARM_ZERO:
                log.info("ch4 gas zero setting, deviceNum: {}", config.getDeviceNum());
                builder.replace(1, 2, "1");
                break;
            case CH4_GAS_VOLUME_ALARM_RANGE:
                log.info("ch4 gas range setting, deviceNum: {}", config.getDeviceNum());
                builder.replace(3, 4, "1");
                builder.replace(4, 6, String.format("%02x", config.getConfVal().intValue()));
                break;
            case TEMPERATURE_CORRECTION:
                log.info("temperature setting, deviceNum: {}", config.getDeviceNum());
                builder.replace(7, 8, "1");
                builder.replace(8, 10, String.format("%02x", config.getConfVal().intValue()));
                break;
            case UPLOAD_DATA_PERIOD:
                log.info("upload time setting, deviceNum: {}", config.getDeviceNum());
                builder.replace(86, 90, String.format("%04x", config.getConfVal().intValue()));
                break;
            default:
                break;
        }
    }

    @Override
    public void addOriginalData(String bodyStr, String deviceNum) {
        int num = dao.addOriginalData(bodyStr, deviceNum, new Date());
        log.info("add original data success, num: {}, deviceNum: {}", num, deviceNum);
    }

    @Override
    public List<OriginalData> findOriginalDataList(TerminalDeviceDTO dto) {
        return dao.findOriginalDataList(dto);
    }

    /**
     * 解析设备上传探测器基本信息（A1），并构建填充设备对象
     */
    private void buildDeviceUploadDetails(TerminalDevice device, String data) {
        List<TerminalDeviceConfig> alarmConfigs = terminalDeviceService.findConfigList(new TerminalDeviceDTO(device.getDeviceNum(), ALARM_TYPE));
        List<TerminalDeviceConfigDTO> updateList = new ArrayList<>(7);
        for (TerminalDeviceConfig config : alarmConfigs) {
            setConfVal(data, config);
            updateList.add(buildConfig2DTO(config));
        }

        terminalDeviceService.updateConfigByDeviceNum(updateList, ALARM_TYPE);
    }

    private void setConfVal(String data, TerminalDeviceConfig config) {
        switch (Objects.requireNonNull(RecordEnum.of(config.getConfType()))) {
            case WELL_LID_OPEN_ALARM:
                config.setConfVal(getWellLidAlarmOpenStatus(data).doubleValue());
                break;
            case WATER_DEPTH_ALARM_THRESHOLD:
                config.setConfVal(getWaterDepthThreshold(data));
                break;
            case CH4_GAS_VOLUME_THRESHOLD:
                config.setConfVal(getGasConcentrationThreshold(data));
                break;
            case TEMPERATURE_THRESHOLD:
                config.setConfVal(getTemperatureThreshold(data));
                break;
            case DEVICE_BAT_VOL_THRESHOLD:
                config.setConfVal(getDeviceBatVolThreshold(data));
                break;
            case DEVICE_UPLOAD_DATA_PERIOD:
                config.setConfVal(BusinessUtil.getDeviceUploadTime(data));
                break;
        }
    }

    private TerminalDeviceConfigDTO buildConfig2DTO(TerminalDeviceConfig config) {
        return new TerminalDeviceConfigDTO(config.getDeviceNum(), config.getConfType(), config.getConfVal());
    }

    /**
     * 解析设备上传的采集信息功能码（A2），并构建填充设备对象
     */
    private void buildDeviceUploadFunction(TerminalDevice device, String data) {
        alarmService.addDeviceAlarm(getSystemErrorCode(data), device.getDeviceNum(), device.getLocation());

        List<TerminalDeviceDetail> list = new ArrayList<>();

        resolveWaterDepthStatus(device, data, list);
        resolveCh4Status(device, data, list);
        resolveWellLidStatus(device, data, list);
        resolveDeviceBatVol(device, data, list);

        terminalDeviceService.updateDevice(device);

        if (CollectionUtils.isEmpty(list)) {
            log.info("terminalDeviceDetail list is empty");
            return;
        }

        terminalDeviceDetailService.batchAddDetail(list);
    }

    /**
     * 解析并保存设备电池电量
     */
    private void resolveDeviceBatVol(TerminalDevice device, String data, List<TerminalDeviceDetail> list) {
        device.setDeviceBatVol(getDeviceBatVol(data));
        list.add(buildTerminalDeviceDetail(device, DEVICE_BAT_VOL));
        log.info("get deviceBatVol: {}, deviceNum: {}", device.getDeviceBatVol(), device.getDeviceNum());
    }

    /**
     * 解析并保存井盖倾斜状态及井盖电池电量
     */
    private void resolveWellLidStatus(TerminalDevice device, String data, List<TerminalDeviceDetail> list) {
        if (!Objects.equals(ENABLED, getWellLidStatus(data))) {
            log.info("wellLidStatus is Disabled, deviceNum: {}", device.getDeviceNum());
            return;
        }

        DeviceAlarmType alarmType = alarmService.findDeviceAlarmType(device.getDeviceNum());
        if (Objects.equals(alarmType.getWellLidStatus(), DISABLED)) {
            log.info("device alarm type: wellLidStatus is Disabled, deviceNum: {}", device.getDeviceNum());
            return;
        }

        device.setWellLidOpenStatus(getWellLidOpenStatus(data));
        device.setWellLidBatVol(getWellLidBatVol(data));

        list.add(buildTerminalDeviceDetail(device, WELL_LID_BAT_VOL));
        log.info("wellLidStatus is Enabled, wellLidOpenStatus: {}, wellLidBatVol: {}, deviceNum: {}",
                device.getWellLidOpenStatus(), device.getWellLidBatVol(), device.getDeviceNum());
    }

    /**
     * 解析并保存甲烷气体浓度及甲烷传感器内温度
     */
    private void resolveCh4Status(TerminalDevice device, String data, List<TerminalDeviceDetail> list) {
        if (!Objects.equals(ENABLED, getCH4GasSensorStatus(data))) {
            log.info("ch4GasSensorStatus is Disabled, deviceNum: {}", device.getDeviceNum());
            return;
        }

        device.setTemperature(getCH4GasTemperature(data));
        device.setCh4GasConcentration(getCH4GasConcentration(data));

        list.add(buildTerminalDeviceDetail(device, CH4_CONCENTRATION));
        list.add(buildTerminalDeviceDetail(device, TEMPERATURE));

        log.info("ch4GasSensorStatus is Enabled, ch4GasConcentration: {}, temperature: {}, deviceNum: {}",
                device.getCh4GasConcentration(), device.getTemperature(), device.getDeviceNum());
    }

    /**
     * 解析并保存液位深度
     */
    private void resolveWaterDepthStatus(TerminalDevice device, String data, List<TerminalDeviceDetail> list) {
        if (!Objects.equals(ENABLED, getWaterSensorStatus(data))) {
            log.info("waterSensorStatus is Disabled, deviceNum: {}", device.getDeviceNum());
            return;
        }

        device.setWaterDepth(getWaterDepth(data));
        list.add(buildTerminalDeviceDetail(device, WATER_DEPTH));
        log.info("waterSensorStatus is Enabled, waterDepth: {}, deviceNum: {}", device.getWaterDepth(), device.getDeviceNum());
    }

    /**
     * 构建设备登录时数据
     */
    private void buildDeviceLoginCode(TerminalDevice device, String data) {
        device.setDeviceBatVol(getLoginBatVol(data));
        device.setUpdateUser(SYSTEM_USER);

        terminalDeviceService.updateDevice(device);
        terminalDeviceDetailService.batchAddDetail(Collections.singletonList(buildTerminalDeviceDetail(device, DEVICE_BAT_VOL)));
    }
}

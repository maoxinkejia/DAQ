package com.qcxk.service.impl;

import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.dao.DeviceAlarmDao;
import com.qcxk.dao.TerminalDeviceDetailDao;
import com.qcxk.model.VO.TerminalDataDetailVO;
import com.qcxk.model.alarm.DeviceAlarmDetail;
import com.qcxk.model.device.TerminalDeviceDetail;
import com.qcxk.service.TerminalDeviceDetailService;
import com.qcxk.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.qcxk.common.Constants.*;
import static com.qcxk.util.BusinessUtil.buildTerminalDeviceDetailListVO;

@Slf4j
@Service
public class TerminalDeviceDetailServiceImpl implements TerminalDeviceDetailService {
    @Autowired
    private TerminalDeviceDetailDao dao;

    @Autowired
    private DeviceAlarmDao deviceAlarmDao;


    @Override
    public void batchAddDetail(List<TerminalDeviceDetail> list) {
        int num = dao.batchAdd(list);
        log.info("add terminal device detail success, num: {}", num);
    }

    @Override
    public TerminalDataDetailVO findList(String startDate, String endDate, String deviceNum) throws ParseException {
        Date start = DateUtils.parseDate(startDate, DateUtils.yyyy_MM_dd);
        Date end = DateUtils.getNextDate(endDate);

        List<TerminalDeviceDetail> ch4ConcentrationList = dao.findList(start, end, deviceNum, CH4_CONCENTRATION);
        List<TerminalDeviceDetail> waterDepthList = dao.findList(start, end, deviceNum, WATER_DEPTH);
        List<TerminalDeviceDetail> deviceBatVolList = dao.findList(start, end, deviceNum, DEVICE_BAT_VOL);
        List<TerminalDeviceDetail> temperatureList = dao.findList(start, end, deviceNum, TEMPERATURE);
        List<TerminalDeviceDetail> wellLidBatVolList = dao.findList(start, end, deviceNum, WELL_LID_BAT_VOL);
        List<DeviceAlarmDetail> alarmList = deviceAlarmDao.findAlarmListByDeviceNum(deviceNum);

        TerminalDataDetailVO vo = new TerminalDataDetailVO();
        vo.setCh4GasConcentrationList(buildTerminalDeviceDetailListVO(ch4ConcentrationList));
        vo.setWaterDepthList(buildTerminalDeviceDetailListVO(waterDepthList));
        vo.setTemperatureList(buildTerminalDeviceDetailListVO(temperatureList));
        vo.setDeviceBatVolList(buildTerminalDeviceDetailListVO(deviceBatVolList));
        vo.setWellLidBatVolList(buildTerminalDeviceDetailListVO(wellLidBatVolList));
        vo.setAlarmList(alarmList);

        return vo;
    }

    @Override
    public List<TerminalDeviceDetail> findOriginalDataList(TerminalDeviceDTO dto) throws ParseException {
        Date start = null;
        Date end = null;

        if (StringUtils.isNotBlank(dto.getStartDate())) {
            start = DateUtils.parseDate(dto.getStartDate(), DateUtils.yyyy_MM_dd);
        }
        if (StringUtils.isNotBlank(dto.getEndDate())) {
            end = DateUtils.getNextDate(dto.getEndDate());
        }

        return dao.findOriginalDataList(dto.getDeviceNum(), start, end);
    }
}

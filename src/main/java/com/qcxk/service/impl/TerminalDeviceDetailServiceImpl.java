package com.qcxk.service.impl;

import com.qcxk.dao.DeviceAlarmDao;
import com.qcxk.dao.TerminalDeviceDetailDao;
import com.qcxk.model.DeviceAlarmDetail;
import com.qcxk.model.TerminalDeviceDetail;
import com.qcxk.model.VO.TerminalDataDetailVO;
import com.qcxk.service.TerminalDeviceDetailService;
import com.qcxk.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.qcxk.common.Constants.*;

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
        List<TerminalDeviceDetail> batVolList = dao.findList(start, end, deviceNum, BAT_VOL);
        List<TerminalDeviceDetail> temperatureList = dao.findList(start, end, deviceNum, TEMPERATURE);
        List<DeviceAlarmDetail> alarmList = deviceAlarmDao.findAlarmListByDeviceNum(deviceNum);

        TerminalDataDetailVO vo = new TerminalDataDetailVO();
        vo.setCh4GasConcentrationList(ch4ConcentrationList);
        vo.setWaterDepthList(waterDepthList);
        vo.setTemperatureList(temperatureList);
        vo.setBatVolList(batVolList);
        vo.setAlarmList(alarmList);

        return vo;
    }
}

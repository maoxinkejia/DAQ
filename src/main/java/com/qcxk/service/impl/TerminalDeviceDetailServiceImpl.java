package com.qcxk.service.impl;

import com.qcxk.dao.TerminalDeviceDetailDao;
import com.qcxk.model.TerminalDeviceDetail;
import com.qcxk.service.TerminalDeviceDetailService;
import com.qcxk.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TerminalDeviceDetailServiceImpl implements TerminalDeviceDetailService {
    @Autowired
    private TerminalDeviceDetailDao dao;


    @Override
    public void batchAddDetail(List<TerminalDeviceDetail> list) {
        int num = dao.batchAdd(list);
        log.info("add terminal device detail success, num: {}", num);
    }

    @Override
    public List<TerminalDeviceDetail> findList(String startDate, String endDate) throws ParseException {
        Date start = DateUtils.parseDate(startDate, DateUtils.YYYY_MM_DD);
        Date end = DateUtils.getNextDate(endDate);
        return dao.findList(start, end);
    }


}

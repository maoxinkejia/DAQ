package com.qcxk.service;

import com.qcxk.model.TerminalDeviceDetail;
import com.qcxk.model.VO.TerminalDataDetailVO;

import java.text.ParseException;
import java.util.List;

public interface TerminalDeviceDetailService {
    void batchAddDetail(List<TerminalDeviceDetail> list);

    TerminalDataDetailVO findList(String startDate, String endDate, String deviceNum) throws ParseException;
}

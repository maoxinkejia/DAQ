package com.qcxk.service;

import com.qcxk.model.VO.TerminalDataDetailVO;
import com.qcxk.model.device.TerminalDeviceDetail;

import java.text.ParseException;
import java.util.List;

public interface TerminalDeviceDetailService {
    void batchAddDetail(List<TerminalDeviceDetail> list);

    TerminalDataDetailVO findList(String startDate, String endDate, String deviceNum) throws ParseException;
}

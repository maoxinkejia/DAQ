package com.qcxk.service;

import com.qcxk.model.TerminalDeviceDetail;

import java.text.ParseException;
import java.util.List;

public interface TerminalDeviceDetailService {
    void batchAddDetail(List<TerminalDeviceDetail> list);

    List<TerminalDeviceDetail> findList(String startDate, String endDate) throws ParseException;
}

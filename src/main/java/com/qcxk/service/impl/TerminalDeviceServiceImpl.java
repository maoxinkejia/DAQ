package com.qcxk.service.impl;

import com.qcxk.dao.TerminalDeviceDao;
import com.qcxk.model.TerminalDevice;
import com.qcxk.model.TerminalDeviceConfig;
import com.qcxk.service.TerminalDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TerminalDeviceServiceImpl implements TerminalDeviceService {
    @Autowired
    private TerminalDeviceDao dao;

    @Override
    public TerminalDevice findByDeviceNum(String deviceNum) {
        return dao.findByDeviceNum(deviceNum);
    }

    @Override
    public TerminalDevice add(TerminalDevice device) {
        return dao.add(device);
    }

    @Override
    public void updateBatVolAndRssi(Integer batVol, Integer rssi, Integer terminalId) {
        dao.updateBatVolAndRssi(batVol, rssi, terminalId, new Date());
    }

    @Override
    public void updateDevice(TerminalDevice device) {
        dao.update(device);
    }

    @Override
    public List<TerminalDeviceConfig> findConfigByDeviceNum(String deviceNum) {
        return null;
    }
}

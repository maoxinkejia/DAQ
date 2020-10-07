package com.qcxk.service;

import com.qcxk.model.TerminalDevice;

public interface TerminalDeviceService {
    TerminalDevice findByDeviceNum(String deviceNum);

    TerminalDevice add(TerminalDevice device);

    void updateBatVolAndRssi(Integer batVol, Integer rssi, Integer terminalId);

    void updateDevice(TerminalDevice device);
}

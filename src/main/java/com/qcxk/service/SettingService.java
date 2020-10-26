package com.qcxk.service;

import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.model.device.TerminalDeviceConfig;

import java.util.List;

public interface SettingService {
    List<TerminalDeviceConfig> findList(TerminalDeviceDTO dto);
}

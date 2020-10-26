package com.qcxk.service.impl;

import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.dao.TerminalDeviceDao;
import com.qcxk.model.device.TerminalDeviceConfig;
import com.qcxk.service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private TerminalDeviceDao dao;

    @Override
    public List<TerminalDeviceConfig> findList(TerminalDeviceDTO dto) {
        return dao.findConfigList(dto);
    }
}

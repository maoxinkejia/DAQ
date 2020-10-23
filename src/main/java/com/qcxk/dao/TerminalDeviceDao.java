package com.qcxk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.model.device.TerminalDevice;
import com.qcxk.model.device.TerminalDeviceConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminalDeviceDao extends BaseMapper<TerminalDevice> {
    TerminalDevice findByDeviceNum(String deviceNum);

    int update(TerminalDevice device);

    List<TerminalDevice> findList(TerminalDeviceDTO dto);

    void updateBootTime(TerminalDevice device);

    int batchAddTerminalDeviceConfigs(List<TerminalDeviceConfig> configs);

    List<TerminalDeviceConfig> findConfigByDeviceNum(String deviceNum);
}

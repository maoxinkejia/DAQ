package com.qcxk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcxk.model.TerminalDevice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TerminalDeviceDao extends BaseMapper<TerminalDevice> {
    TerminalDevice findByDeviceNum(String deviceNum);

    TerminalDevice add(TerminalDevice device);

    void updateBatVolAndRssi(@Param("batVol") Integer batVol, @Param("rssi") Integer rssi,
                             @Param("terminalId") Integer terminalId, @Param("bootTime") Date date);

    void update(TerminalDevice device);
}

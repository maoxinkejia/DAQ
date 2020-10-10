package com.qcxk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.model.TerminalDevice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TerminalDeviceDao extends BaseMapper<TerminalDevice> {
    TerminalDevice findByDeviceNum(String deviceNum);

    TerminalDevice add(TerminalDevice device);

    void updateBatVolAndBootTime(@Param("batVol") Integer batVol, @Param("deviceNum") String deviceNum, @Param("bootTime") Date date);

    void update(TerminalDevice device);

    List<TerminalDevice> findList(TerminalDeviceDTO dto);
}

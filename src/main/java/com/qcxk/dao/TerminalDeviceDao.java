package com.qcxk.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcxk.controller.model.query.TerminalDeviceConfigDTO;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.model.VO.TerminalDataListVO;
import com.qcxk.model.device.TerminalDevice;
import com.qcxk.model.device.TerminalDeviceConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerminalDeviceDao extends BaseMapper<TerminalDevice> {
    TerminalDevice findByDeviceNum(String deviceNum);

    int update(TerminalDevice device);

    List<TerminalDevice> findList(TerminalDeviceDTO dto);

    int batchAddTerminalDeviceConfigs(List<TerminalDeviceConfig> configs);

    List<TerminalDeviceConfig> findConfigByDeviceNum(@Param("deviceNum") String deviceNum, @Param("settingType") Integer settingType);

    int updateDeviceConfig(TerminalDeviceConfigDTO configDTO);

    int addTerminalDevice(TerminalDevice device);

    int updateDeviceSendStatus(@Param("deviceNum") String deviceNum, @Param("sendStatus") Integer sendStatus);

    List<TerminalDeviceConfig> findChangedConfByDeviceNum(String deviceNum);

    int updateApplyTime(String deviceNum);

    int updateAlarmTime(String deviceNum);

    int updateConfigSendStatus(TerminalDeviceConfig config);

    int updateConfig2Deleted(String deviceNum);

    List<TerminalDataListVO> findDataList(TerminalDeviceDTO dto);
}

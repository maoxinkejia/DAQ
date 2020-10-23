package com.qcxk.service;

import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.model.VO.TerminalDataListVO;
import com.qcxk.model.device.TerminalDevice;
import com.qcxk.model.device.TerminalDeviceConfig;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TerminalDeviceService {
    TerminalDevice findByDeviceNum(String deviceNum);

    TerminalDevice add(TerminalDevice device);

    void updateDevice(TerminalDevice device);

    List<TerminalDeviceConfig> findConfigByDeviceNum(String deviceNum);

    List<TerminalDevice> findList(TerminalDeviceDTO dto);

    void updateBootTime(TerminalDevice device);

    String uploadDeviceImages(MultipartFile[] files);

    List<TerminalDataListVO> findDataList(TerminalDeviceDTO dto);

    void delete(String deviceNum);
}

package com.qcxk.controller;

import com.qcxk.controller.model.query.TerminalDeviceConfigDTO;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.controller.model.response.Response;
import com.qcxk.model.device.TerminalDeviceConfig;
import com.qcxk.service.TerminalDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/setting")
public class SettingController {

    @Autowired
    private TerminalDeviceService terminalDeviceService;

    @GetMapping(value = "/list")
    public Response getList(TerminalDeviceDTO dto) {
        List<TerminalDeviceConfig> list = terminalDeviceService.findConfigList(dto);

        return Response.build(list).success();
    }

    @PutMapping(value = "/update")
    public Response updateSetting(TerminalDeviceConfigDTO dto) {
        terminalDeviceService.updateConfigByDeviceNum(dto);
        return Response.build().success();
    }
}

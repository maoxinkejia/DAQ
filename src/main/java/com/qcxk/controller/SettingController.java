package com.qcxk.controller;

import com.github.pagehelper.PageHelper;
import com.qcxk.controller.model.page.Pagination;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.controller.model.response.PageResponse;
import com.qcxk.controller.model.response.Response;
import com.qcxk.model.device.TerminalDeviceConfig;
import com.qcxk.service.SettingService;
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
    private SettingService settingService;

    @GetMapping(value = "/list")
    public Response getList(TerminalDeviceDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<TerminalDeviceConfig> list = settingService.findList(dto);

        Pagination pagination = Pagination.buildPagination(list, dto);
        return PageResponse.pageResponse(pagination, list).success();
    }

    @PutMapping(value = "/update")
    public Response updateSetting() {

        return Response.build().success();
    }
}

package com.qcxk.controller;

import com.github.pagehelper.PageHelper;
import com.qcxk.controller.model.page.Pagination;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.controller.model.response.PageResponse;
import com.qcxk.controller.model.response.Response;
import com.qcxk.model.device.TerminalDeviceDetail;
import com.qcxk.service.MessageService;
import com.qcxk.service.TerminalDeviceDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/message")
public class MessageController {
    @Autowired
    private TerminalDeviceDetailService terminalDeviceDetailService;

    @GetMapping(value = "/list")
    public Response getList(TerminalDeviceDTO dto) throws ParseException {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<TerminalDeviceDetail> list = terminalDeviceDetailService.findOriginalDataList(dto);

        Pagination pagination = Pagination.buildPagination(list, dto);
        return PageResponse.pageResponse(pagination, list).success();
    }
}

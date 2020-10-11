package com.qcxk.controller;

import com.qcxk.controller.model.page.Pagination;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.controller.model.response.PageResponse;
import com.qcxk.controller.model.response.Response;
import com.qcxk.model.TerminalDevice;
import com.qcxk.service.TerminalDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "terminal")
public class TerminalController {

    @Autowired
    private TerminalDeviceService service;

    @GetMapping(value = "terminalList")
    public Response getTerminalList(@RequestBody TerminalDeviceDTO dto) {
        List<TerminalDevice> list = service.findList(dto);

        Pagination pagination = Pagination.buildPagination(list, dto);
        return PageResponse.pageResponse(pagination, list).success();
    }

    @PostMapping(value = "add")
    public Response addTerminalDevice(@RequestBody TerminalDevice device, @RequestBody MultipartFile[] files) {
        service.uploadDeviceImages(files, device);
        service.add(device);
        return Response.build().success();
    }
}

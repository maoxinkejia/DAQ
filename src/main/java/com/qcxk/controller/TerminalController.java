package com.qcxk.controller;

import com.github.pagehelper.PageHelper;
import com.qcxk.controller.model.page.Pagination;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.controller.model.response.PageResponse;
import com.qcxk.controller.model.response.Response;
import com.qcxk.model.VO.TerminalDataDetailVO;
import com.qcxk.model.VO.TerminalDataListVO;
import com.qcxk.model.device.TerminalDevice;
import com.qcxk.model.device.TerminalDeviceConfig;
import com.qcxk.service.TerminalDeviceDetailService;
import com.qcxk.service.TerminalDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/terminal")
public class TerminalController {
    @Autowired
    private TerminalDeviceService service;
    @Autowired
    private TerminalDeviceDetailService detailService;

    @GetMapping(value = "/terminalList")
    public Response getTerminalList(TerminalDeviceDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());

        List<TerminalDevice> list = service.findList(dto);
        log.info("find terminalDevice list size: {}", list.size());

        Pagination pagination = Pagination.buildPagination(list, dto);
        return PageResponse.pageResponse(pagination, list).success();
    }

    @PostMapping(value = "/addDevice")
    public Response addTerminalDevice(@RequestBody TerminalDevice device) {
        service.add(device);
        return Response.build().success();
    }

    @PostMapping(value = "/upload/{deviceNum}")
    public Response uploadPicture(@PathVariable("deviceNum") String deviceNum, @RequestBody List<MultipartFile> files) {
        service.uploadDeviceImages(files, deviceNum);
        return Response.build().success();
    }

    @PutMapping(value = "/updateDevice")
    public Response updateTerminalDevice(@RequestBody TerminalDevice device) {
        service.updateDevice(device);
        return Response.build().success();
    }

    @DeleteMapping(value = "/deleteDevice/{deviceNum}")
    public Response deleteTerminalDevice(@NotBlank(message = "deviceNum不能为空") @PathVariable(value = "deviceNum") String deviceNum) {
        service.delete(deviceNum);
        return Response.build().success();
    }

    @GetMapping(value = "/getDeviceDetail")
    public Response getDeviceDetail(String deviceNum) {
        List<TerminalDeviceConfig> details = service.findConfigByDeviceNum(deviceNum);
        return Response.build(details).success();
    }

    @GetMapping(value = "/dataList")
    public Response getDataList(TerminalDeviceDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<TerminalDataListVO> list = service.findDataList(dto);

        Pagination pagination = Pagination.buildPagination(list, dto);
        return PageResponse.pageResponse(pagination, list).success();
    }

    @GetMapping(value = "/data/details")
    public Response getDataDetail(String startDate, String endDate, String deviceNum) throws ParseException {
        TerminalDataDetailVO vo = detailService.findList(startDate, endDate, deviceNum);
        return Response.build(vo).success();
    }
}

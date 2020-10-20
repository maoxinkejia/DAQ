package com.qcxk.controller;

import com.github.pagehelper.PageHelper;
import com.qcxk.controller.model.page.Pagination;
import com.qcxk.controller.model.query.TerminalDeviceDTO;
import com.qcxk.controller.model.response.PageResponse;
import com.qcxk.controller.model.response.Response;
import com.qcxk.model.TerminalDevice;
import com.qcxk.model.TerminalDeviceConfig;
import com.qcxk.model.TerminalDeviceDetail;
import com.qcxk.model.VO.TerminalDataDetailVO;
import com.qcxk.model.VO.TerminalDataListVO;
import com.qcxk.service.TerminalDeviceDetailService;
import com.qcxk.service.TerminalDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Response getTerminalList(Integer page, Integer pageSize,
                                    String deviceNum, String tubeWellDescription, String location,
                                    Integer controlStatus, Integer delStatus) {

        TerminalDeviceDTO dto = buildRequestParam(page, pageSize, deviceNum, tubeWellDescription, location, controlStatus, delStatus);
        PageHelper.startPage(dto.getPage(), dto.getPageSize());

        List<TerminalDevice> list = service.findList(dto);

        Pagination pagination = Pagination.buildPagination(list, dto);
        return PageResponse.pageResponse(pagination, list).success();
    }

    @PostMapping(value = "/addDevice")
    public Response addTerminalDevice(@RequestBody TerminalDevice device) {
        service.add(device);
        return Response.build().success();
    }

    @PostMapping(value = "/upload")
    public Response uploadPicture(@RequestBody MultipartFile[] files) {
        String imagePath = service.uploadDeviceImages(files);
        return Response.build(imagePath).success();
    }

    @PutMapping(value = "/updateDevice")
    public Response updateTerminalDevice(@RequestBody TerminalDevice device, @RequestBody MultipartFile[] files) {
        // TODO
        return Response.build().success();
    }

    @GetMapping(value = "/getDeviceDetail")
    public Response getDeviceDetail(String deviceNum) {
        List<TerminalDeviceConfig> details = service.findConfigByDeviceNum(deviceNum);
        return Response.build(details).success();
    }

    @GetMapping(value = "/dataList")
    public Response getDataList(String deviceNum, String location, Integer delStatus) {
        TerminalDeviceDTO dto = buildRequestParam(null, null, deviceNum, null, location, null, delStatus);
        List<TerminalDataListVO> list = service.findDataList(dto);
        return Response.build(list).success();
    }

    @GetMapping(value = "/data/details")
    public Response getDataDetail(String startDate, String endDate, String deviceNum) throws ParseException {
        TerminalDataDetailVO vo = detailService.findList(startDate, endDate, deviceNum);
        return Response.build(vo).success();
    }


    private TerminalDeviceDTO buildRequestParam(Integer page, Integer pageSize, String deviceNum, String tubeWellDescription, String location, Integer controlStatus, Integer delStatus) {
        TerminalDeviceDTO dto = new TerminalDeviceDTO();
        dto.setPage(page);
        dto.setPageSize(pageSize);
        dto.setDeviceNum(deviceNum);
        dto.setTubeWellDescription(tubeWellDescription);
        dto.setLocation(location);
        dto.setControlStatus(controlStatus);
        dto.setDelStatus(delStatus);

        return dto;
    }
}

package com.qcxk.controller;

import com.github.pagehelper.PageHelper;
import com.qcxk.controller.model.page.Pagination;
import com.qcxk.controller.model.query.AlarmDTO;
import com.qcxk.controller.model.response.PageResponse;
import com.qcxk.controller.model.response.Response;
import com.qcxk.model.alarm.DeviceAlarmDetail;
import com.qcxk.model.alarm.DeviceAlarmType;
import com.qcxk.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/alarm")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @GetMapping(value = "/alarmType")
    public Response getDeviceAlarmType(String deviceNum) {
        DeviceAlarmType alarmType = alarmService.findDeviceAlarmType(deviceNum);
        return Response.build(alarmType).success();
    }

    @GetMapping(value = "/alarmList")
    public Response getAlarmList(AlarmDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        List<DeviceAlarmDetail> list = alarmService.findAlarmList(dto);

        Pagination pagination = Pagination.buildPagination(list, dto);
        return PageResponse.pageResponse(pagination, list).success();
    }

    @PutMapping(value = "/apply/{id}")
    public Response applyAlarm(@PathVariable(value = "id") Long id) {
        alarmService.applyAlarm(id);
        return Response.build().success();
    }

    @PutMapping(value = "/apply/{deviceNum}")
    public Response applyByDeviceNum(@PathVariable(value = "deviceNum") String deviceNum) {
        alarmService.applyByDeviceNum(deviceNum);
        return Response.build().success();
    }

    @PutMapping(value = "/updateAlarmType")
    public Response updateAlarmType(@RequestBody DeviceAlarmType alarmType) {
        alarmService.updateAlarmType(alarmType);
        return Response.build().success();
    }
}

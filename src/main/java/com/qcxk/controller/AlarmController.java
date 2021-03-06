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

    @PutMapping(value = "/apply/{id}/{username}")
    public Response applyAlarm(@PathVariable("id") Long id, @PathVariable("username") String username) {
        alarmService.applyAlarm(id, username);
        return Response.build().success();
    }

    @PutMapping(value = "/apply2/{deviceNum}/{username}")
    public Response applyByDeviceNum(@PathVariable("deviceNum") String deviceNum, @PathVariable("username") String username) {
        alarmService.applyByDeviceNum(deviceNum, username);
        return Response.build().success();
    }

    @PutMapping(value = "/updateAlarmType")
    public Response updateAlarmType(@RequestBody DeviceAlarmType alarmType) {
        alarmService.updateAlarmType(alarmType);
        return Response.build().success();
    }
}

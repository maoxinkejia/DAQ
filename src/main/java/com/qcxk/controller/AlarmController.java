package com.qcxk.controller;

import com.qcxk.controller.model.response.Response;
import com.qcxk.model.DeviceAlarmType;
import com.qcxk.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

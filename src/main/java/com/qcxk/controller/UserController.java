package com.qcxk.controller;

import com.qcxk.controller.model.response.Response;
import com.qcxk.model.user.User;
import com.qcxk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/add")
    public Response addUser(@RequestBody User user) {
        userService.addUser(user);
        return Response.build().success();
    }

    @PutMapping(value = "/update")
    public Response updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Response.build().success();
    }

    @DeleteMapping(value = "/delete/{id}")
    public Response deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return Response.build().success();
    }

}

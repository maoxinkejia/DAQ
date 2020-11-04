package com.qcxk.controller;

import com.github.pagehelper.PageHelper;
import com.qcxk.common.Constants;
import com.qcxk.controller.model.page.Pagination;
import com.qcxk.controller.model.query.UserDTO;
import com.qcxk.controller.model.response.PageResponse;
import com.qcxk.controller.model.response.Response;
import com.qcxk.model.user.User;
import com.qcxk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/list")
    public Response getList(UserDTO dto) {
        PageHelper.startPage(dto.getPage(), dto.getPageSize());

        List<User> list = userService.findList(dto);
        log.info("find user list size: {}", list.size());

        Pagination pagination = Pagination.buildPagination(list, dto);
        return PageResponse.pageResponse(pagination, list).success();
    }

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

    @PostMapping(value = "/login")
    public Response checkPassword(@RequestBody User user) {
        boolean success = userService.findByPassword(user);
        if (success) {
            return Response.build().success();
        }

        return Response.build().fail(Constants.PASSWORD_ERROR);
    }

    @PutMapping(value = "/reset")
    public Response resetPassword(@RequestBody User user) {
        userService.resetPassword(user);
        return Response.build().success();
    }
}

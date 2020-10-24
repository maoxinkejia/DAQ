package com.qcxk.controller;

import com.qcxk.controller.model.response.Response;
import com.qcxk.model.user.User;
import com.qcxk.service.UserService;
import com.qcxk.util.BusinessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.qcxk.common.Constants.LOGIN_SUCCESS;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public Response login(@RequestBody User user, HttpServletResponse response) throws IOException {
        String msg = userService.login(user);

        if (Objects.equals(LOGIN_SUCCESS, msg)) {
            response.addCookie(new Cookie("token", BusinessUtil.getCookie()));
            return Response.build(msg).success();
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("");
        return Response.build().fail(300, msg);
//        return Response.build().fail(300, "");
    }
}

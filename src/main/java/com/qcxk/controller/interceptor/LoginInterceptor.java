package com.qcxk.controller.interceptor;

import com.qcxk.common.Constants;
import com.qcxk.service.UserService;
import com.qcxk.util.BusinessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Objects.equals("/login", request.getRequestURI())) {
            return true;
        }

        return checkLoginStatus(request, response);
    }

    private boolean checkLoginStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies.length == 0 || (!checkLoginCookie(cookies))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(Constants.LOGIN_FAILURE);

            log.warn("login failed, does not has cookie");
            return false;
        }

        return true;
    }

    private boolean checkLoginCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> Objects.equals("token", cookie.getName()))
                .anyMatch(cookie -> Objects.equals(BusinessUtil.getCookie(), cookie.getValue()));
    }
}

package com.qcxk.controller.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RegistryConfiguration implements WebMvcConfigurer{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration loginInterceptor = registry.addInterceptor(new LoginInterceptor());
        InterceptorRegistration privilegeInterceptor = registry.addInterceptor(new privilegeInterceptor());

        loginInterceptor.addPathPatterns("/**");
        privilegeInterceptor.addPathPatterns("/**");
    }
}

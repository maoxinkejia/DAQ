package com.qcxk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.DecimalFormat;
@EnableScheduling
@EnableCaching
@ServletComponentScan
@SpringBootApplication
@MapperScan(basePackages="com.qcxk.dao")
public class ThmsApplication extends SpringBootServletInitializer {
    // 重写 configure方法
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ThmsApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ThmsApplication.class);
        ApplicationContext ac = app.run(args);
    }
}


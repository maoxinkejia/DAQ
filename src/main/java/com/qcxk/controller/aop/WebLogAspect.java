package com.qcxk.controller.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class WebLogAspect {

    @Pointcut("execution(* com.qcxk.controller.*.*(..))")
    public void pointLog() {
    }

    @Before("pointLog()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("BODY: {}", Arrays.toString(joinPoint.getArgs()));
    }
}

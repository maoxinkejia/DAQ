package com.qcxk.controller.exception;

import com.qcxk.controller.model.response.Response;
import com.qcxk.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler
    public Response onException(Exception e) {
        if (e instanceof ParamException) {
            log.error("request param error");
        }
        return Response.build().fail(e.getMessage());
    }
}

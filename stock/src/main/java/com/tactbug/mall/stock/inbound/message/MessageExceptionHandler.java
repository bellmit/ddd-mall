package com.tactbug.mall.stock.inbound.message;

import com.tactbug.mall.common.base.TactException;
import com.tactbug.mall.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice(basePackages = "com.tactbug.mall.stock.inbound.message")
public class MessageExceptionHandler {

    @ExceptionHandler(TactException.class)
    @ResponseBody
    public void tactExceptionHandler(TactException tactException){
        log.error(ExceptionUtil.getMessage(tactException));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public void exceptionHandler(Exception e){
        log.error(ExceptionUtil.getMessage(e));
    }
}

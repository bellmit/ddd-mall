package com.tactbug.mall.stock.inbound.web;

import com.tactbug.mall.common.base.TactException;
import com.tactbug.mall.common.utils.ExceptionUtil;
import com.tactbug.mall.common.vo.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "com.tactbug.mall.stock.inbound.web")
@Slf4j
public class ExceptionController {

    @ExceptionHandler(TactException.class)
    @ResponseBody
    public ResultResponse tactExceptionHandler(TactException tactException){
        log.error(ExceptionUtil.getMessage(tactException));
        return ResultResponse.error(tactException);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultResponse exceptionHandler(Exception e){
        log.error(ExceptionUtil.getMessage(e));
        return ResultResponse.error();
    }
}

package com.lzj.admin.GlobalExceptionHandler;

import com.lzj.admin.exceptions.ParamsException;
import com.lzj.admin.model.RespBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author sunyj
 * @Date 2022/3/7 13:41
 * @Version 1.0
 */
@ControllerAdvice

public class GlobalExceptionHandler {

    //参数异常
    @ExceptionHandler(ParamsException.class)
    @ResponseBody
    public RespBean paramException(ParamsException e){
        return RespBean.error(e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    //其他异常
    public RespBean exception(Exception e){
        return RespBean.error(e.getMessage());
    }
}

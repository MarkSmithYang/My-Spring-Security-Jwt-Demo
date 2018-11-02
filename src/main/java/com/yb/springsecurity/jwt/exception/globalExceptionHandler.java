package com.yb.springsecurity.jwt.exception;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yangbiao
 * @Description:controller层的异常统一捕捉处理类
 * @date 2018/11/2
 */
@RestControllerAdvice
//@Profile(value = {"dev","test"})//可以指定捕捉处理的环境
public class globalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParameterErrorException.class)
    public JSONObject parameterErrorException(ParameterErrorException e){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST.value());
        jsonObject.put("message", e.getMessage());//这里的获取到的信息就是自定义的信息,因为父类的信息被覆盖了
        return jsonObject;
    }
}

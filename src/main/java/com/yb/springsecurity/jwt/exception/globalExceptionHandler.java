package com.yb.springsecurity.jwt.exception;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;

/**
 * @author yangbiao
 * @Description:controller层的异常统一捕捉处理类
 * @date 2018/11/2
 */
@RestControllerAdvice
//@Profile(value = {"dev","test"})//可以指定捕捉处理的环境
public class globalExceptionHandler {
    public static final Logger log = LoggerFactory.getLogger(globalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParameterErrorException.class)
    public JSONObject parameterErrorExceptionHandler(ParameterErrorException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST.value());
        //这里的获取到的信息就是自定义的信息,因为父类的信息被覆盖了
        jsonObject.put("message", e.getMessage());
        return jsonObject;
    }

    /**
     * jwt验证秘钥(签名)异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SignatureException.class)
    public JSONObject signatureExceptionHandler(SignatureException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST.value());
        jsonObject.put("message", e.getMessage());
        return jsonObject;
    }

    /**
     * jwt的时间过期异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpiredJwtException.class)
    public JSONObject expiredJwtExceptionHandler(ExpiredJwtException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST.value());
        jsonObject.put("message", e.getMessage());
        return jsonObject;
    }

    /**
     * 运行时异常捕获处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public JSONObject runtimeExceptionHandler(RuntimeException e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST.value());
        jsonObject.put("message", "网络异常");
        log.info("运行时异常:" + e.getMessage());
        return jsonObject;
    }

    /**
     * Exception异常捕获处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public JSONObject exceptionHandler(Exception e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", HttpStatus.BAD_REQUEST.value());
        jsonObject.put("message", "网络异常");
        log.info("Exception异常:" + e.getMessage());
        return jsonObject;
    }

}

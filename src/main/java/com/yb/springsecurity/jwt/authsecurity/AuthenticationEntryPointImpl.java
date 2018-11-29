package com.yb.springsecurity.jwt.authsecurity;

import com.alibaba.fastjson.JSONObject;
import com.yb.springsecurity.jwt.common.ResultInfo;
import com.yb.springsecurity.jwt.controller.SecurityJwtController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangbiao
 * @Description:统一处理未登录的无权访问的类
 * @date 2018/11/20
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setStatus(401);
        ResultInfo info = ResultInfo.status(HttpServletResponse.SC_UNAUTHORIZED).message("请登录");
        response.getOutputStream().write(JSONObject.toJSON(info).toString().getBytes());
//        JSONObject jsonObject = new JSONObject(true);
//        jsonObject.put("status", HttpStatus.UNAUTHORIZED.value());
//        jsonObject.put("message", "请登录");
//        //处理输出到页面-->这种方式直接就是把json对象输出到页面,比较原始,不能设置字体颜色等
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "application/json;charset=UTF-8");
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.getOutputStream().write(jsonObject.toJSONString().getBytes());
        //通过此方式可以和error.html配置th:text获取值,但是有点难看
        //response.sendError(HttpStatus.UNAUTHORIZED.value(), "请登录");
    }
}


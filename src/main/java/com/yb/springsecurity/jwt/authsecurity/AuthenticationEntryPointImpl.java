package com.yb.springsecurity.jwt.authsecurity;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yangbiao
 * @Description:统一处理未登录的无权访问的类
 * @date 2018/11/20
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        JSONObject jsonObject = new JSONObject(true);
        jsonObject.put("status", HttpStatus.UNAUTHORIZED.value());
        jsonObject.put("message", "请登录");
        response.sendError(HttpServletResponse.SC_OK, "哈哈哈哈哈哈");
        //处理输出到页面
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        response.getOutputStream().write(jsonObject.toJSONString().getBytes());
    }
}

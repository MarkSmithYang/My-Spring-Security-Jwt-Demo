package com.yb.springsecurity.jwt.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.yb.springsecurity.jwt.abc.AntiViolenceCheck;
import com.yb.springsecurity.jwt.common.CommonDic;
import com.yb.springsecurity.jwt.common.ResultInfo;
import com.yb.springsecurity.jwt.model.SysUser;
import com.yb.springsecurity.jwt.request.UserRequest;
import com.yb.springsecurity.jwt.response.Token;
import com.yb.springsecurity.jwt.service.SecurityJwtService;
import com.yb.springsecurity.jwt.utils.RealIpGetUtils;
import com.yb.springsecurity.jwt.utils.VerifyCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangbiao
 * @Description:控制层代码
 * @date 2018/11/19
 */
@Api("我的controller测试")
@RestController
@CrossOrigin//处理跨域
@RequestMapping("/security")//添加一层路径是必要的
public class SecurityJwtController {
 public static final Logger log = LoggerFactory.getLogger(SecurityJwtController.class);

    @Autowired
    private SecurityJwtService securityJwtService;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletResponse response) {
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        try {
            VerifyCodeUtils.outputImage(80, 30, response.getOutputStream(), verifyCode);
        } catch (IOException e) {
            log.info("验证码输出异常");
            e.printStackTrace();
        }
    }

    @ApiOperation("yes的查询")
    @GetMapping("/yes")
    public ResultInfo<List<String>> yes() {
        return ResultInfo.success(new ArrayList<String>() {{
            add("yes");
        }});
    }

    @ApiOperation("hello的查询")
    @GetMapping("/hello")
    public ResultInfo<List<String>> hello() {
        return ResultInfo.success(new ArrayList<String>() {{
            add("hello");
        }});
    }

    @ApiOperation("world的查询")
    @GetMapping("/world")
    public ResultInfo<List<String>> world() {
        return ResultInfo.success(new ArrayList<String>() {{
            add("world");
        }});
    }

    @ApiOperation("users的查询")
    @GetMapping("/users")
    public ResultInfo<List<String>> users() {
        return ResultInfo.success(new ArrayList<String>() {{
            add("rose");
            add("jack");
            add("mark");
        }});
    }

    @ApiOperation("前端登录")
    @GetMapping("/frontLogin")
    public ResultInfo<Token> frontLogin(@Valid UserRequest userRequest, HttpServletRequest request) {
        //获取用户名
        String username = userRequest.getUsername();
        //获取sessionid
        String sessionId = request.getSession().getId();
        //获取服务真实地址
        String ipAddress = RealIpGetUtils.getIpAddress(request);
        //拼接存储key用以存储信息到redis
        String idKey = CommonDic.LOGIN_SIGN_PRE + ipAddress + sessionId;
        String key = CommonDic.LOGIN_SIGN_PRE + ipAddress + username;
        //获取输入的验证码
        String verifyCode = userRequest.getVerifyCode();
        //校验验证码

        return ResultInfo.success(null);
    }


}

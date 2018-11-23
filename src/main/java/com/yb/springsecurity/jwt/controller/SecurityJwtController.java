package com.yb.springsecurity.jwt.controller;

import com.yb.springsecurity.jwt.authsecurity.AntiViolenceCheck;
import com.yb.springsecurity.jwt.authsecurity.CustomAuthenticationProvider;
import com.yb.springsecurity.jwt.common.CommonDic;
import com.yb.springsecurity.jwt.common.ResultInfo;
import com.yb.springsecurity.jwt.exception.ParameterErrorException;
import com.yb.springsecurity.jwt.model.SysUser;
import com.yb.springsecurity.jwt.request.UserRequest;
import com.yb.springsecurity.jwt.response.Token;
import com.yb.springsecurity.jwt.service.SecurityJwtService;
import com.yb.springsecurity.jwt.utils.RealIpGetUtils;
import com.yb.springsecurity.jwt.utils.VerifyCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yangbiao
 * @Description:控制层代码
 * @date 2018/11/19
 */
@Api("我的controller测试")
@Controller
@CrossOrigin//处理跨域
@RequestMapping("/security")//添加一层路径是必要的
public class SecurityJwtController {
 public static final Logger log = LoggerFactory.getLogger(SecurityJwtController.class);

    @Autowired
    private SecurityJwtService securityJwtService;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/verifyCode")
    @ResponseBody
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
    @ResponseBody
    public ResultInfo<List<String>> yes() {
        return ResultInfo.success(new ArrayList<String>() {{
            add("yes");
        }});
    }

    @ApiOperation("hello的查询")
    @GetMapping("/hello")
    @ResponseBody
    public ResultInfo<List<String>> hello() {
        return ResultInfo.success(new ArrayList<String>() {{
            add("hello");
        }});
    }

    @ApiOperation("world的查询")
    @GetMapping("/world")
    @ResponseBody
    public ResultInfo<List<String>> world() {
        return ResultInfo.success(new ArrayList<String>() {{
            add("world");
        }});
    }

    @ApiOperation("users的查询")
    @GetMapping("/users")
    @ResponseBody
    public ResultInfo<List<String>> users() {
        return ResultInfo.success(new ArrayList<String>() {{
            add("rose");
            add("jack");
            add("mark");
        }});
    }

    @ApiOperation("前端登录")
    @GetMapping("/frontLogin")
    @ResponseBody
    public ResultInfo<Token> frontLogin(@Valid UserRequest userRequest, HttpServletRequest request) {
        //获取用户名
        String username = userRequest.getUsername();
        //获取用户真实地址
        String ipAddress = RealIpGetUtils.getIpAddress(request);
        //拼接存储key用以存储信息到redis
        String key = CommonDic.LOGIN_SIGN_PRE + ipAddress + username;
        //更新用户登录次数
        AntiViolenceCheck.updateLoginTimes(redisTemplate,request,username);
        //根据用户输入的用户名获取用户信息
        SysUser sysUser = securityJwtService.findByUsername(username);
        //判断用户是否存在
        if(sysUser!=null){
            //用户登录认证
            AntiViolenceCheck.authUser(sysUser,request,CommonDic.FROM_FRONT,customAuthenticationProvider);
        }else {
            //如果登录失败次数大于等于规定的正常次数时,增加登录次数的过期时间
            AntiViolenceCheck.loginError(key,redisTemplate);
        }

        return ResultInfo.success(null);
    }

    @GetMapping("/verifyCodeCheck")
    @ResponseBody
    public String verifyCodeCheck(String verifyCode, HttpServletRequest request) {
        if (StringUtils.isNotBlank(verifyCode)) {
            //获取服务ip
            String ipAddress = RealIpGetUtils.getIpAddress(request);
            String key = CommonDic.VERIFYCODE_SIGN_PRE + ipAddress;
            //获取redis上的存储的(最新的)验证码
            String code = (String) redisTemplate.opsForValue().get(key);
            //校验验证码
            if (StringUtils.isNotBlank(code) && code.contains("@&")) {
                code = code.split("@&")[1];
                if (verifyCode.toLowerCase().equals(code.toLowerCase())) {
                    return "true";
                }
            } else {
                return "expir";
            }
        }
        return "false";
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletResponse response, HttpServletRequest request, Model model) {
        Integer times;
        //获取服务ip
        String ipAddress = RealIpGetUtils.getIpAddress(request);
        //拼接存储redis的key
        String key = CommonDic.VERIFYCODE_SIGN_PRE + ipAddress;
        //获取验证码及其刷新次数信息
        String code = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(code) && code.contains("@&")) {
            times = Integer.valueOf(code.split("@&")[0]);
            //判断刷新次数
            if (times > CommonDic.REQUEST_MAX_TIMES) {
                //结束程序--等待redis上的数据过期再重新再来
                return;
            }
            //增加次数
            times++;
        } else {
            times = 0;
        }
        //获取字符验证码
        String verifyCode = VerifyCodeUtils.generateVerifyCode(CommonDic.VERIFYCODE_AMOUNT);
        try {
            VerifyCodeUtils.outputImage(80, 30, response.getOutputStream(), verifyCode);
            //存储验证码并设置过期时间为5分钟--限制点击的次数,防止恶意点击
            redisTemplate.opsForValue().set(key, times + "@&" + verifyCode, CommonDic.VERIFYCODE_EXPIRED, TimeUnit.SECONDS);
        } catch (IOException e) {
            log.info("验证码输出异常");
            e.printStackTrace();
        }
    }

}

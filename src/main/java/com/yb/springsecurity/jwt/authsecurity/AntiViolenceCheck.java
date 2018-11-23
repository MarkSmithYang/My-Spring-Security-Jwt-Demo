package com.yb.springsecurity.jwt.authsecurity;


import com.yb.springsecurity.jwt.common.CommonDic;
import com.yb.springsecurity.jwt.exception.ParameterErrorException;
import com.yb.springsecurity.jwt.model.SysUser;
import com.yb.springsecurity.jwt.utils.RealIpGetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Description:反暴力检查类--就是防止恶意攻击的
 * author yangbiao
 * date 2018/11/21
 */
public class AntiViolenceCheck {
    public static final Logger log = LoggerFactory.getLogger(AntiViolenceCheck.class);
    //要用的静态常量设置
    private static final long FIFTEEN_MINUTES = 15 * 60;
    private static final long ONE_DAY = 24 * 60 * 60;
    private static final long ONE_HOUR = 60 * 60;
    private static final int FORBIDDEN_TIMES = 5;
    private static final int IP_FORBIDDEN_TIMES = 100;

    /**
     * 更新用户登录的次数
     * @param redisTemplate
     * @param request
     * @param username
     */
    public static void updateLoginTimes(RedisTemplate<String, Serializable> redisTemplate,
                                        HttpServletRequest request, String username) {
        //获取用户真实地址
        String ipAddress = RealIpGetUtils.getIpAddress(request);
        //拼接存储key用以存储信息到redis
        String key = CommonDic.LOGIN_SIGN_PRE + ipAddress + username;
        //获取redis上存储的登录次数
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        //判断次数是否为空
        if (times == null) {
            times = 0;
        }
        //增加登录次数
        if (times < Integer.MAX_VALUE) {
            times++;
        }
        //存储更新后的数据
        redisTemplate.opsForValue().set(key, times, 30, TimeUnit.MINUTES);
    }

    /**
     * 用户登录失败到一定次数,增加其等待时间
     * @param key
     * @param redisTemplate
     */
    public static void loginError(String key, RedisTemplate<String, Serializable> redisTemplate) {
        //获取redis上存储的登录次数
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        //判断次数是否为空
        if (times == null) {
            times = 0;
        }
        //判断登录失败的次数是否在正常范围内
        if(times>=CommonDic.FORMAL_LOGIN_TIMES){
            Double retryTime = Math.pow(2, times-CommonDic.FORMAL_LOGIN_TIMES);
            //如果重试时间超过了Integer最大值的1/2的时候,就不让其再变大了
            if (retryTime > Integer.MAX_VALUE / 2) {
                retryTime = Integer.MAX_VALUE / 2 + 0D;
            }
            //获取重试时间的int值
            int addTime = retryTime.intValue();
            //更新延长了过期时间的times
            redisTemplate.opsForValue().set(key, times,addTime+FIFTEEN_MINUTES,TimeUnit.MINUTES);
            //返回提示信息
            log.info("用户登录失败次数的过多");
            ParameterErrorException.message("用户名或密码错误");
        }
    }

    /**
     * 用户的登录认证
     * @param sysUser
     * @param request
     * @param fromFront
     * @param customAuthenticationProvider
     */
    public static void authUser(SysUser sysUser, HttpServletRequest request, String fromFront,
                                CustomAuthenticationProvider customAuthenticationProvider) {
        //获取获取到的用户名和密码
        String username = sysUser.getUsername();
        String password = sysUser.getPassword();
        //构造Token类(自定义)
        UsernamePasswordAuthenticationToken token = new MyUsernamePasswordAuthenticationToken(username, password, fromFront);
        //调用自定义的用户认证Provider认证用户
        Authentication authenticate = customAuthenticationProvider.authenticate(token);
        //把认证信息存储安全上下文
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        //
    }
}

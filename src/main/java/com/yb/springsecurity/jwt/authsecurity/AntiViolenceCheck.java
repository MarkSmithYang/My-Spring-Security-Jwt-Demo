package com.yb.springsecurity.jwt.authsecurity;

import com.yb.springsecurity.jwt.common.CommonDic;
import com.yb.springsecurity.jwt.exception.ParameterErrorException;
import com.yb.springsecurity.jwt.exception.RetryTimeException;
import com.yb.springsecurity.jwt.utils.RealIpGetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

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
     *
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
     *
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
        if (times >= CommonDic.FORMAL_LOGIN_TIMES) {
            Double retryTime = Math.pow(2, times - CommonDic.FORMAL_LOGIN_TIMES);
            //如果重试时间超过了Integer最大值的1/2的时候,就不让其再变大了
            if (retryTime > Integer.MAX_VALUE / 2) {
                retryTime = Integer.MAX_VALUE / 2 + 0D;
            }
            //获取重试时间的int值
            int addTime = retryTime.intValue();
            //更新延长了过期时间的times
            redisTemplate.opsForValue().set(key, times, addTime + FIFTEEN_MINUTES, TimeUnit.MINUTES);
            //返回提示信息
            log.info("用户登录失败次数的过多");
            ParameterErrorException.message("用户名或密码错误");
        }
    }

    /**
     * 检查电话登录次数
     */
    public static void checkMobileLoginTimes(HttpServletRequest request,
                                             RedisTemplate<String, Serializable> redisTemplate, String username) {
        //获取ip拼接redis的key
        String ip = RealIpGetUtils.getIpAddress(request);
        String key = CommonDic.LOGIN_TIMES_PRE + ip + username;
        //获取key的过期时间
        Long retryTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (retryTime == null) {
            retryTime = 0L;
        }
        //获取次数
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        if (times == null) {
            times = 0;
        }
        //判断次数是否超过指定数值
        if (times < FORBIDDEN_TIMES) {
            times++;
        }
        //增加更新其过期时间
        redisTemplate.opsForValue().set(key, times, retryTime + FIFTEEN_MINUTES, TimeUnit.SECONDS);
        //判断次数是否符合规定数值并做相应的操作
        if (times > CommonDic.CHECK_LOGIN_TIMES && retryTime > FIFTEEN_MINUTES) {
            Double retry = Math.pow(times - 1, 2);
            retryTime = retry.longValue();
            //更新过期时间
            redisTemplate.opsForValue().set(key, times, retryTime + FIFTEEN_MINUTES, TimeUnit.SECONDS);
            RetryTimeException.message("操作频繁，请" + retryTime + "秒后重试");
        }
    }

    /**
     * 禁用用户名设置
     */
    public static synchronized void usernameOneDayForbidden(RedisTemplate<String, Serializable> redisTemplate,
                                                            String username) {
        //拼接redis的key
        String key = AntiViolenceCheck.class.getName() +
                CommonDic._USERNAME_ONE_DAY_FORBIDDEN_ + username;
        //获取登录的
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        if (times == null) {
            times = 0;
        }
        if (times < FORBIDDEN_TIMES) {
            times++;
            redisTemplate.opsForValue().set(key, times, ONE_DAY, TimeUnit.SECONDS);
        } else {
            RetryTimeException.message("您的操作频繁,该帐号一天之内不能再登陆");
        }

    }

    /**
     * 清除用户名不能登录设置
     */
    public static synchronized void usernameOneDayClear(RedisTemplate<String, Serializable> redisTemplate,
                                                        String username) {
        //拼接key
        String key = AntiViolenceCheck.class.getName() + CommonDic._USERNAME_ONE_DAY_FORBIDDEN_ + username;
        //获取用户名并清除
        Object object = redisTemplate.opsForValue().get(key);
        if (object != null) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断ip是否该禁用
     */
    public static void ipForbidden(HttpServletRequest request, RedisTemplate<String, Serializable> redisTemplate) {
        //获取ip并拼接key
        String ip = RealIpGetUtils.getIpAddress(request);
        String key = AntiViolenceCheck.class.getName() + CommonDic._IP_FORBIDDEN_ + ip;
        //获取用户登录次数
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        if (times == null) {
            times = 0;
        }
        //判断登录次数是否超过指定值并更新数据
        if (times < IP_FORBIDDEN_TIMES) {
            times++;
            //更新redis上的更新数据,设置次数过期时间为1个小时
            redisTemplate.opsForValue().set(key, times, ONE_HOUR, TimeUnit.SECONDS);
        } else {
            //更新redis上的更新数据,设置次数过期时间为1个小时
            redisTemplate.opsForValue().set(key, times, ONE_DAY, TimeUnit.SECONDS);
            RetryTimeException.message("您的操作频繁，您的IP已经被禁一天");
        }
    }

    /**
     * 清空取消ip禁用
     */
    public static void ipForbiddenClear(HttpServletRequest request, RedisTemplate<String, Serializable> redisTemplate) {
        //获取ip并拼接key
        String ip = RealIpGetUtils.getIpAddress(request);
        String key = AntiViolenceCheck.class.getName() + CommonDic._IP_FORBIDDEN_ + ip;
        //清空redis上的数据
        redisTemplate.opsForValue().set(key, 0, 0, TimeUnit.SECONDS);
    }
}

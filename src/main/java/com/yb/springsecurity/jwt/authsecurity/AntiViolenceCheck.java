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
 * Description:反暴力检查类--就是防止恶意攻击的,通过在登录的时候调用相应的方法,
 * 记录次数,超出指定的次数就抛出异常,因为只要redis上的次数的过期时间没到,就会一直抛出异常,
 * 让其一直处理登录拒绝的状态,当然了redis服务挂了,也会因为取不到次数,异常而停止程序的
 * 账户的禁用是不管你在哪台电脑登录都不会成功,ip禁用仅仅只是某台电脑,因为一个是用用户名
 * 拼接指定字符串作为key存储次数,一个是用ip拼接,还有就是手机验证码发送次数太多的禁用,ip和用户名都用
 * 也就是电脑和用户名相同的情况下,超过指定次数就不会发送信息的
 * 在用户登录成功的时候需要清空记录的次数,不然随着时间的推移,真确的用户也会被拒绝的
 * 用户名这里5次就禁用了,而ip是100次,也就是某台电脑最多可用20个账号全部错误的登录
 * ------------------------------------------------------------------------
 * 如果使用jwt的token放在每个请求接口的Header里的话,就不必写这个类了,因为可以直接在网关过滤器那里,
 * 进行token的合法校验,可以直接在过滤器里返回信息,而不用请求服务接口,直到其实合法的token才行,不像这个一样,安全用户(业务是在一起的)
 * author yangbiao
 * date 2018/11/21
 */
public class AntiViolenceCheck {
    public static final Logger log = LoggerFactory.getLogger(AntiViolenceCheck.class);
    //要用的静态常量设置
    private static final long FIFTEEN_MINUTES = 15;//分钟
    private static final long ONE_DAY_MINUTES = 24 * 60;//分钟
    private static final long THIRTY_MINUTES = 30;//分钟
    public static final int FORMAL_LOGIN_TIMES = 5;
    public static final int PHONE_LOGIN_TIMES = 2;
    public static final int PHONE_LOGIN_TIMES_MAX = 5;
    private static final int USERNAME_FORBIDDEN_TIMES = 5;
    private static final int IP_FORBIDDEN_TIMES = 100;

    /**
     * 验证用户名以前先检查用户是否多次错误登录,这样就能避免恶意程序让你一直去访问数据库服务
     * 只有登录错误的次数在允许的范围内再去验证用户名和密码
     */
    public static void checkLoginTimes(RedisTemplate<String, Serializable> redisTemplate, String key) {
        //获取redis上存储的登录次数
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        //判断次数是否为空
        if (times == null) {
            times = 0;
        }
        //增加登录次数--虽然还不知道用户登录是否成功就给它记录了一次,但是登录成功后会把它清空
        //或者减1来判断即可
        if (times < Integer.MAX_VALUE) {
            if (times <= FORMAL_LOGIN_TIMES) {
                //增加次数
                times++;
                //存储更新后的数据
                redisTemplate.opsForValue().set(key, times, THIRTY_MINUTES, TimeUnit.MINUTES);
            } else {
                //超过指定的登录失败次数增加失败次数的过期时间
                Double retryTime = Math.pow(2, times - FORMAL_LOGIN_TIMES);
                //如果重试时间超过了Integer最大值的1/2的时候,就不让其再变大了
                if (retryTime > Integer.MAX_VALUE / 2) {
                    retryTime = Integer.MAX_VALUE / 2 + 0D;
                }
                //获取登录失败次数的过期时间
                Long expire = redisTemplate.getExpire(key, TimeUnit.MINUTES);
                //获取重试时间的int值
                Long addTime = retryTime.intValue() + expire;
                //更新失败次数,次数越多,等待时间越长
                times++;
                redisTemplate.opsForValue().set(key, times, addTime + THIRTY_MINUTES, TimeUnit.MINUTES);
                //返回提示信息
                ParameterErrorException.message("您登录失败的次数过多,请" + addTime + "分钟后再登陆");
            }
        }
    }

    /**
     * 清零用户登录失败的次数
     */
    public static void checkLoginTimesClear(RedisTemplate<String, Serializable> redisTemplate, String key) {
        //清零redis上的数据,最好需要删除key,不然会占用大量的redis存储空间
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        if (times != null) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 检查电话登录次数--检测到超出次数就抛出异常中断程序
     */
    public static void checkPhoneLoginTimes(HttpServletRequest request, RedisTemplate<String, Serializable>
            redisTemplate, String username) {
        //获取ip拼接redis的key
        String ip = RealIpGetUtils.getIpAddress(request);
        String key = CommonDic.LOGIN_TIMES_PRE + ip + username;
        //获取key的过期时间
        Long retryTime = redisTemplate.getExpire(key, TimeUnit.MINUTES);
        if (retryTime == null) {
            retryTime = 0L;
        }
        //获取次数
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        if (times == null) {
            times = 0;
        }
        //每次登录增加15分钟过期时间
        //判断次数是否超过指定数值
        if (times <= PHONE_LOGIN_TIMES) {
            times++;
            //增加更新其过期时间--每登录一次,登录失败次数过期时间就延长15分钟
            retryTime = retryTime + FIFTEEN_MINUTES;
            //连续两次登录失败则过期时间接近30分钟3~5次登录每次加15分钟
            redisTemplate.opsForValue().set(key, times, retryTime, TimeUnit.MINUTES);
        } else {
            if (times > PHONE_LOGIN_TIMES_MAX) {
                //当登录次数大于5次的时候,直接禁止其发送消息一天
                redisTemplate.opsForValue().set(key, times, ONE_DAY_MINUTES, TimeUnit.MINUTES);
                RetryTimeException.message("操作过于频繁，请" + ONE_DAY_MINUTES * 60 + "秒后重试");
            }
            //生成等待时间
            Double retry = Math.pow(times - 1, 2);
            retryTime = retry.longValue();
            //更新过期时间
            times++;
            redisTemplate.opsForValue().set(key, times, retryTime, TimeUnit.MINUTES);
            RetryTimeException.message("操作频繁，请" + retryTime * 60 + "秒后重试");
        }
    }

    /**
     * 禁用用户名设置--检测到超出次数就抛出异常中断程序
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
        if (times <= USERNAME_FORBIDDEN_TIMES) {
            times++;
            redisTemplate.opsForValue().set(key, times, ONE_DAY_MINUTES, TimeUnit.MINUTES);
        } else {
            Long expire = redisTemplate.getExpire(key, TimeUnit.MINUTES);
            RetryTimeException.message("您的操作频繁,请" + expire + "分钟后再登陆");
        }
    }

    /**
     * 清除用户名不能登录设置--登录成功需要清零该用户名登录的次数
     */
    public static synchronized void usernameOneDayClear(RedisTemplate<String, Serializable> redisTemplate,
                                                        String username) {
        //拼接key
        String key = AntiViolenceCheck.class.getName() + CommonDic._USERNAME_ONE_DAY_FORBIDDEN_ + username;
        //把改用户名登录失败的次数清零,最好需要删除key,不然会占用大量的redis存储空间
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        if (times != null) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断ip是否该禁用--检测到超出次数就抛出异常中断程序
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
        if (times <= IP_FORBIDDEN_TIMES) {
            times++;
            //更新redis上的更新数据,设置次数过期时间为1天
            redisTemplate.opsForValue().set(key, times, ONE_DAY_MINUTES, TimeUnit.MINUTES);
        } else {
            //获取过期时间提醒用户
            Long expire = redisTemplate.getExpire(key, TimeUnit.MINUTES);
            RetryTimeException.message("您的操作频繁，请" + expire + "分钟后再试");
        }
    }

    /**
     * 清空取消ip禁用--登录成功需要清零禁用的次数
     */
    public static void ipForbiddenClear(HttpServletRequest request,
                                        RedisTemplate<String, Serializable> redisTemplate) {
        //获取ip并拼接key
        String ip = RealIpGetUtils.getIpAddress(request);
        String key = AntiViolenceCheck.class.getName() + CommonDic._IP_FORBIDDEN_ + ip;
        //清零redis上的数据,最好需要删除key,不然会占用大量的redis存储空间
        Integer times = (Integer) redisTemplate.opsForValue().get(key);
        if (times != null) {
            redisTemplate.delete(key);
        }
    }
}

package com.yb.springsecurity.jwt.common;

/**
 * @author yangbiao
 * @Description:关于token的静态变量
 * @date 2018/11/20
 */
public class CommonDic {
    //请求头Header里的token的key静态变量
    public static final String HEADER_NAME = "Authentication";
    public static final String RE_HEADLER_NAME = "retoken";
    //过期时间设置
    public static final long TOKEN_EXPIRE = 30;//分钟
    public static final long RETOKEN_EXPIRE = 7 * 24 * 60;//分钟
    //ajxa处理类用
    public static final String SECURITY_CONTEXT = "SECURITY_CONTEXT";
    //登录的来源
    public static final String FROM_FRONT = "front";//前台
    public static final String FROM_BACK = "back";//后台
    //登陆的前缀字典
    public static final String LOGIN_SIGN_PRE = "LOGIN_SIGN_PRE";
}

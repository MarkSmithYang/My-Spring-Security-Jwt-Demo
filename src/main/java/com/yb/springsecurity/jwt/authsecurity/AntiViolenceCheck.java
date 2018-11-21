package com.yb.springsecurity.jwt.authsecurity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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




}

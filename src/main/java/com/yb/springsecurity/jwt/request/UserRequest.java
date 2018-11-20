package com.yb.springsecurity.jwt.request;

/**
 * @author yangbiao
 * @Description:封装用户登录信息的类
 * @date 2018/11/20
 */
public class UserRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否记住密码
     */
    private boolean isRemember;

    /**
     * 验证码--这里设置登录错误三次就需要输入验证码,防止程序攻击
     */
    private String verifyCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return isRemember;
    }

    public void setRemember(boolean remember) {
        isRemember = remember;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}

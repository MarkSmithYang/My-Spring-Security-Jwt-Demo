package com.yb.springsecurity.jwt.response;

import com.yb.springsecurity.jwt.model.SysUser;

import java.io.Serializable;
import java.util.Set;

/**
 * @author yangbiao
 * @Description:登录成功后返回的信息的封装类
 * @date 2018/11/20
 */
public class Token{

    /**
     * 用户信息--我这里就只用基本信息了,如果需要用户的详细信息可以自己创建类去封装即可
     */
    private SysUser sysUser;

    /**
     * 用户的角色信息
     */
    private Set<String> roles;

    /**
     * 用户的权限信息
     */
    private Set<String> permissions;

    /**
     * 用户的模块(菜单)信息
     */
    private Set<String> modules;

    /**
     * 是否过期
     */
    private boolean expired;

    /**
     * 是否是图片验证码
     */
    private boolean isImgCode;

    /**
     * 是否是文本验证码
     */
    private boolean isTextCode;

    /**
     * token的字符串信息
     */
    private String token;

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getModules() {
        return modules;
    }

    public void setModules(Set<String> modules) {
        this.modules = modules;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isImgCode() {
        return isImgCode;
    }

    public void setImgCode(boolean imgCode) {
        isImgCode = imgCode;
    }

    public boolean isTextCode() {
        return isTextCode;
    }

    public void setTextCode(boolean textCode) {
        isTextCode = textCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

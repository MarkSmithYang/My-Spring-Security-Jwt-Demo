package com.yb.springsecurity.jwt.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * @author yangbiao
 * @Description:系统用户-->用户信息-->这里全部笼统的放置用户信息了
 * @date 2018/11/2
 */
@Entity
@Table//这里就使用默认的映射策略
public class SysUser implements Serializable {
    private static final long serialVersionUID = -4454755005986723821L;

    @Id//这个注解和@Table一样不能少@Column是可以少的
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户权限
     */
    @ManyToMany(targetEntity = Permission.class,mappedBy = "users",fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    /**
     * 用户角色
     */
    @ManyToMany(targetEntity = Role.class,fetch = FetchType.EAGER)
    private Set<Role> roles;

    public SysUser() {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.createTime = LocalDateTime.now();
    }

    public SysUser(String username, String password) {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.username = username;
        this.password = password;
        this.createTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

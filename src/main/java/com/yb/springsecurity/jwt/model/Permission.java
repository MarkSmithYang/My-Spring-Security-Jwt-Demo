package com.yb.springsecurity.jwt.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yangbiao
 * @Description:权限信息类
 * @date 2018/11/2
 */
@Entity
@Table//这里就使用默认的映射策略
public class Permission implements Serializable {
    private static final long serialVersionUID = -5566183753194600505L;

    @Id//这个注解和@Table一样不能少@Column是可以少的
    private String id;

    /**
     * 权限
     */
    private String permission;

    /**
     * 权限中文
     */
    private String permissionCn;

    /**
     * 权限角色
     */
    @ManyToMany(targetEntity = Role.class, mappedBy = "permissions")
    private Set<Role> roles;

    /**
     * 权限用户
     */
    @ManyToMany(targetEntity = SysUser.class)
    private Set<SysUser> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermissionCn() {
        return permissionCn;
    }

    public void setPermissionCn(String permissionCn) {
        this.permissionCn = permissionCn;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<SysUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SysUser> users) {
        this.users = users;
    }
}

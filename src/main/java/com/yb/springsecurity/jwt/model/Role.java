package com.yb.springsecurity.jwt.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yangbiao
 * @Description:角色信息表
 * @date 2018/11/2
 */
@Entity
@Table//这里就使用默认的映射策略
public class Role implements Serializable {
    private static final long serialVersionUID = -1424025425731168559L;

    @Id//这个注解和@Table一样不能少@Column是可以少的
    private String id;

    /**
     * 角色
     */
    private String role;

    /**
     * 角色中文
     */
    private String roleCn;

    /**
     * 角色权限
     */
    @ManyToMany(targetEntity = Permission.class)
    private Set<Permission> permissions;

    /**
     * 角色用户
     */
    @ManyToMany(targetEntity = SysUser.class, mappedBy = "roles")
    private Set<SysUser> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleCn() {
        return roleCn;
    }

    public void setRoleCn(String roleCn) {
        this.roleCn = roleCn;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<SysUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SysUser> users) {
        this.users = users;
    }
}

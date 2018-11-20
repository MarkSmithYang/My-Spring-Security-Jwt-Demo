package com.yb.springsecurity.jwt.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yangbiao
 * @Description:(菜单)模块类--主要是菜单模块分权
 * @date 2018/11/2
 */
@Entity
@Table//这里就使用默认的映射策略
public class Module implements Serializable {
    private static final long serialVersionUID = -3486259869151800327L;

    @Id//这个注解和@Table一样不能少@Column是可以少的
    private String id;

    /**
     * 模块
     */
    private String module;

    /**
     * 模块中文名
     */
    private String moduleCn;

    /**
     * 模块权限
     */
    @ManyToMany(targetEntity = Permission.class,mappedBy = "modules",fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModuleCn() {
        return moduleCn;
    }

    public void setModuleCn(String moduleCn) {
        this.moduleCn = moduleCn;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}

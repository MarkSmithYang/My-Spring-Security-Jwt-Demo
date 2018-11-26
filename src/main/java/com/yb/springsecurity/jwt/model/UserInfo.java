package com.yb.springsecurity.jwt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Description:用户基本详细信息(根据自己的实际情况封装)
 * author yangbiao
 * date 2018/11/26
 */
@Entity
@Table
@ApiModel("用户基本详细信息")
public class UserInfo {

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户部门")
    private String department;

    @ApiModelProperty("用户职位")
    private String position;

    @ApiModelProperty("用户电话")
    private String phone;

    @ApiModelProperty("基础用户信息")
    @OneToOne(targetEntity = SysUser.class, mappedBy = "userInfo")
    private SysUser sysUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
}

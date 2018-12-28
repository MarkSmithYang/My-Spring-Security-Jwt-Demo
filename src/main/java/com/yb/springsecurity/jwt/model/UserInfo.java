package com.yb.springsecurity.jwt.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description:用户基本详细信息(根据自己的实际情况封装)
 * author yangbiao
 * date 2018/11/26
 */
@Entity
@Table
@ApiModel("用户基本详细信息")
//默认是true,更新的时候只更新修改的那个字段,而不是更新整个表的字段信息,
//从sql就可以看出,性能可以提高很多,插入也是一样的道理
@DynamicUpdate
@DynamicInsert
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -5866848556563329530L;

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户部门")
    private String department;

    @ApiModelProperty("用户职位")
    private String position;

    @ApiModelProperty("用户电话")
    private String phone;

    //外键由没有写mappyed的一方维护,建表的时候会多生成一个外键,
    //直接用这个题是没法封装数据的,因为少了一个属性去封装外键,
    //当了可以直接用SysUser直接get获取
    @ApiModelProperty("基础用户信息")
    @OneToOne(targetEntity = SysUser.class)
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

}

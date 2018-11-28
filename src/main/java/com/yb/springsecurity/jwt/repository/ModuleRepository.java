package com.yb.springsecurity.jwt.repository;

import com.yb.springsecurity.jwt.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author yangbiao
 * Description:其实这个也是没有比较的直接在关联的用户或者权限获取,
 * 当然了获取全部的模块列表的时候是需要的,role和permission也是同理
 * date 2018/11/20
 */
public interface ModuleRepository extends JpaRepository<Module,String> {

}

package com.yb.springsecurity.jwt.repository;

import com.yb.springsecurity.jwt.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yangbiao
 * @Description:
 * @date 2018/11/20
 */
public interface ModuleRepository extends JpaRepository<Module,String> {

}

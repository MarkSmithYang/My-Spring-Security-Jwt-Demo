package com.yb.springsecurity.jwt.service;

import com.yb.springsecurity.jwt.model.SysUser;
import com.yb.springsecurity.jwt.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:服务层代码
 * author yangbiao
 * date 2018/11/21
 */
@Service
public class SecurityJwtService {

    @Autowired
    private SysUserRepository sysUserRepository;

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    public SysUser findByUsername(String username) {
        SysUser sysUser = sysUserRepository.findByUsername(username);
        return sysUser;
    }
}

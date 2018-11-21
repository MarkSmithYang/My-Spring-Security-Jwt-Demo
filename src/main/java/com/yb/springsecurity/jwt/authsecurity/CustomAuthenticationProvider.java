package com.yb.springsecurity.jwt.authsecurity;

import com.yb.springsecurity.jwt.utils.PasswordEncryptUtils;
import com.yb.springsecurity.jwt.exception.ParameterErrorException;
import com.yb.springsecurity.jwt.model.Permission;
import com.yb.springsecurity.jwt.model.Role;
import com.yb.springsecurity.jwt.model.SysUser;
import com.yb.springsecurity.jwt.repository.SysUserRepository;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yangbiao
 * @Description:自定义身份认证类
 * @date 2018/11/19
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {
    public static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取需要认证的用户名
        String username = authentication.getName();
        String name = (String) authentication.getPrincipal();
        String pwd = (String) authentication.getCredentials();
        //获取需要认证的密码
        String password = authentication.getCredentials().toString();
        //进行自定义的逻辑认证
        SysUser sysUser = sysUserRepository.findByUsername(username);
        //判断用户名是否正确
        if (sysUser == null) {
            ParameterErrorException.message("用户名或密码错误");
        }
        //判断用户密码是否正确
        if (!PasswordEncryptUtils.matchPassword(password, sysUser.getPassword())) {
            ParameterErrorException.message("用户名或密码错误");
        }
        //实例化一个装权限的集合类型为GranteAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        //获取用户权限
        Set<Permission> permissions = sysUser.getPermissions();
        //遍历获取权限添加到authorities
        if (CollectionUtils.isNotEmpty(permissions)) {
            //我这里没有写实现类来封装直接用了lambda表达式做的实现
            permissions.forEach(s -> authorities.add(() -> {
                return s.getPermission();
            }));
        }
        //获取用户角色
        Set<Role> roles = sysUser.getRoles();
        //获取角色拥有的权限添加到authorities
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.forEach(s -> {
                if (CollectionUtils.isNotEmpty(s.getPermissions())) {
                    //我这里没有写实现类来封装直接用了lambda表达式做的实现
                    permissions.forEach(a -> authorities.add(() -> {
                        return a.getPermission();
                    }));
                }
            });
        }
        //构建令牌
        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        //是否可以提供输入类型的认证服务
        return false;
    }
}

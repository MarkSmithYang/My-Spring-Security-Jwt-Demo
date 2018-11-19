package com.yb.springsecurity.jwt.auth;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author yangbiao
 * @Description:web的安全配置类
 * @date 2018/11/19
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 设置 HTTP 验证规则
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf验证
        http.csrf().disable()
                //对请求进行认证
                .authorizeRequests()
                //所有带/的请求都放行
                .antMatchers("/").permitAll()
                //所有/login的post请求都放行
                .antMatchers(HttpMethod.POST,"/login").permitAll()
                //访问指定路径的权限校验
                .antMatchers("/hello").hasAnyAuthority("write")
                //访问指定路径的权限校验
                .antMatchers("/yes").hasAnyAuthority("read")
                //访问指定路径的角色校验
                .antMatchers("/world").hasRole("admin")
                //访问指定路径的角色校验(多个角色校验)
                .antMatchers("/users").hasAnyRole("admin,manager")
                //访问指定路径的ip地址校验
                .antMatchers("/yes").hasIpAddress("192.168.11.130")
                //所有请求需要身份认证
                .anyRequest().authenticated().and()
                //添加一个过滤器,所有访问/login的请求都交给JWTLoginFilter来处理
                //这个处理所有JWT相关内容
                .addFilterBefore(new JWTLoginFilter("/login",authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                //添加一个过滤器,对其他请求的token进行合法性认证
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


    }

    /**
     * 使用自定义身份验证组件
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用自定义身份验证(组件)
        auth.authenticationProvider(new CustomAuthenticationProvider());
    }

}

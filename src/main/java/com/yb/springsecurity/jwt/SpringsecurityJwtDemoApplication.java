package com.yb.springsecurity.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement//开启事物支持
public class SpringsecurityJwtDemoApplication{

    public static void main(String[] args) {
        SpringApplication.run(SpringsecurityJwtDemoApplication.class, args);
    }

}











































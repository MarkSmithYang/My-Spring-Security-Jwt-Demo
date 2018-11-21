package com.yb.springsecurity.jwt.config;

import com.sun.javafx.css.Selector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author yangbiao
 * @Description:swagger配置类
 * @date 2018/11/21
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                //添加构建的详细文档信息
                .apiInfo(apiInfo())
                //指定swagger扫描的包路径
                .select().apis(RequestHandlerSelectors.basePackage("com.yb.springsecurity.jwt.controller"))
                //可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                //构建
                .build();
    }

    /**
     * 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     */
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档的标题
                .title("SpringSecurity")
                //设置文档的描述
                .description("小demo")
                //设置文档的联系方式
                .contact(new Contact("SmithYang", "http://www.baidu.com", ""))
                //设置文档的版本信息
                .version("1.0")
                //构建
                .build();
    }

}

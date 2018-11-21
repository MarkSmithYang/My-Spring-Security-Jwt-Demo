package com.yb.springsecurity.jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;

/**
 * @author yangbiao
 * @Description:redisTemplate的实例化配置,这样就可以注入模板使用了
 * @date 2018/11/16
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String server;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        //实测这里必须要添加host不然就会拒绝连接,但是不知道为什么设置的方法过时了,可能只能配置集群的情况吧
        factory.setHostName(server);
        return factory;
    }

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate() {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        //设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        //序列化设置
        JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
        //设置key的序列化
        redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
        //设置value的序列化
        redisTemplate.setValueSerializer(serializer);
        //redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
        //返回模板
        return redisTemplate;
    }
}

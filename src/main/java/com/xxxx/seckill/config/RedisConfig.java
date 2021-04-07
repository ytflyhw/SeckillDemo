package com.xxxx.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类（实现 redis 序列化）
 */

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 配置 redisTemplate
        redisTemplate.setKeySerializer(new StringRedisSerializer());   // redis Key 序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // redis Value 序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());    // redis Hash key 序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer()); // redis Hash value 序列化


        // 注入连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}

package com.cgr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 创建 RedisTemplate 对象，指定 key 类型为 String，value 类型为 Object
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        // 注入 Redis 连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 创建通用的 JSON 序列化器，使用 Jackson2 将对象序列化为 JSON
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        // --------------------- key 序列化设置 ---------------------
        // 使用 Spring Data Redis 提供的静态工厂方法，返回一个 StringRedisSerializer（UTF-8 编码）
        redisTemplate.setKeySerializer(RedisSerializer.string());
        // hash 的 key 同样使用 String 序列化
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        // --------------------- value 序列化设置 ---------------------
        // 普通 value 使用 JSON 序列化
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        // hash 的 value 同样使用 JSON 序列化
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        // 返回配置好的 RedisTemplate
        return redisTemplate;
    }

}

package com.springcloud.framework.core.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class RedisTemplateBuilder {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private ObjectMapper objectMapper;

    public <T> RedisTemplate<String, T> buildRedisTemplate(Class<?> parametrized, Class<?>... parameterClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(javaType);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return buildRedisTemplate(jackson2JsonRedisSerializer);
    }

    public <T> RedisTemplate<String, T> buildRedisTemplate(TypeReference<T> typeReference) {
        JavaType javaType = objectMapper.getTypeFactory().constructType(typeReference);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(javaType);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return buildRedisTemplate(jackson2JsonRedisSerializer);
    }

    private <T> RedisTemplate<String, T> buildRedisTemplate(Jackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}

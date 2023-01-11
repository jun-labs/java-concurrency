package com.example.stock.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

// @Configuration
// @EnableTransactionManagement
public class RedisJdbcConfiguration {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisSessionCallbackRedisTemplate() {
        RedisTemplate<String, Object> redisSessionCallbackRedisTemplate = new RedisTemplate<>();
        redisSessionCallbackRedisTemplate.setConnectionFactory(redisConnectionFactory());
        redisSessionCallbackRedisTemplate.setKeySerializer(new StringRedisSerializer());
        redisSessionCallbackRedisTemplate.setValueSerializer(new StringRedisSerializer());
        redisSessionCallbackRedisTemplate.setEnableTransactionSupport(true);
        return redisSessionCallbackRedisTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}

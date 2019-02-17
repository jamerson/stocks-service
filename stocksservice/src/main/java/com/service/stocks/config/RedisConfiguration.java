package com.service.stocks.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import redis.clients.jedis.JedisPoolConfig;

//TODO: fix this
//@Configuration
//@EnableRedisRepositories
public class RedisConfiguration {
	private String hostname = "server";
    private int port = 6379;
    
    @Bean
    public RedisConnectionFactory connectionFactory() {
      return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {

      RedisTemplate<byte[], byte[]> template = new RedisTemplate<byte[], byte[]>();
      return template;
    }
}

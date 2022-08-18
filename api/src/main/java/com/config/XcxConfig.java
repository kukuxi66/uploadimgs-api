package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

@Configuration
public class XcxConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


}

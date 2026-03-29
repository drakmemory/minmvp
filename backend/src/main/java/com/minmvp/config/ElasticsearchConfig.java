package com.minmvp.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    // Spring Boot auto-configuration handles ES REST client setup
    // Index name constant
    public static final String USER_INDEX = "user_index";
}

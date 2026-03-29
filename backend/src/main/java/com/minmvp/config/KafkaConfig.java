package com.minmvp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {
    // Spring Boot auto-configuration handles most Kafka setup
    // Custom beans can be added here if needed

    public static final String TOPIC_OPERATION_LOG = "user-operation-log";
    public static final String TOPIC_SYSTEM_NOTIFICATION = "system-notification";
}

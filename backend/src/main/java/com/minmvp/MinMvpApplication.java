package com.minmvp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.minmvp.mapper")
@EnableScheduling
public class MinMvpApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinMvpApplication.class, args);
    }
}

package com.cindy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Cindy
 */
@EnableScheduling
@EnableAsync
@MapperScan("com.cindy.mapper")
@SpringBootApplication
public class UserBackgroundApp {
    public static void main(String[] args) {
        SpringApplication.run(UserBackgroundApp.class, args);
    }
}

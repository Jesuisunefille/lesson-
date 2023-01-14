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
@SpringBootApplication     //启动类注解，用于自动装配类和扫描所在包及子包的@Component注解
public class UserBackgroundApp {
    public static void main(String[] args) {
        //启动代码
        SpringApplication.run(UserBackgroundApp.class, args);
    }
}

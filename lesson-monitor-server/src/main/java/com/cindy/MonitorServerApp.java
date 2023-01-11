package com.cindy;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Cindy
 */
@EnableAdminServer //注释以启动监控功能
@SpringBootApplication
public class MonitorServerApp {
    public static void main(String[] args) {
        SpringApplication.run(MonitorServerApp.class, args);
    }
}

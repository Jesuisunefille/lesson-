package com.cindy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author cindy
 */
@Configuration //以声明为配置类
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        /*
         * addMapping：允许跨域访问我所有的URL路径
         * allowedOrigins：允许所有的请求来源对我进行跨域访问
         * allowCredentials：允许发送Cookie信息
         * allowedMethods：允许所有的请求类型对我进行跨域访问
         * allowedHeaders：允许请求中携带任意的Header信息
         * maxAge：设置预检请求的超时时间，默认1800秒
         * */
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600 * 24);
    }
}

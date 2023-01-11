package com.cindy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cindy
 */
@Configuration
public class SwaggerConfig {
    private final static String TITLE = "lesson-user-background项目接口文档";
    private final static String VERSION = "v1.0.1";
    private final static String EMAIL = "2460131984@qq.com";
    private final static String NAME = "Cindy";
    private final static String URL = "http:/localhost:5277";
    private final static String DESCRIPTION = "<em>lesson-user-background子项目是lesson项目的普通用户后台子项目，基于springboot + mybatis框架开发，使用前后端分离技术，用户在未登录状态下，提供浏览首页轮播图，广告，查看视频列表，查看视频详情，搜索视频等功能入口，在登录状态下，提供修改个人信息，注销账号，上传头像，购买视频，加入购物车，查询订单以及删除订单等功能入口，在支付成功状态下，提供在线观看视频，实时发送弹幕等功能入口。</em>";

    /**
     * swagger通用信息展板
     */
    @Bean
    public OpenAPI commonInfo() {
        return new OpenAPI().info(new Info()
                .title(TITLE)
                .description(DESCRIPTION)
                .version(VERSION)
                .contact(new Contact().email(EMAIL).name(NAME).url(URL)));
    }
}
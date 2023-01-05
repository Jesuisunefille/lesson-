package com.cindy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cindy
 */
@RestController
public class NginxController {

    @GetMapping("execute")
    public String execute(){
        return "我是nginx-b项目";
    }
}

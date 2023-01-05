package com.cindy.controller;

import com.cindy.entity.Banner;
import com.cindy.service.BannerService;
import com.cindy.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author cindy
 */
@Slf4j
@Controller
@RequestMapping("/api/v1/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Operation(summary = "查询最多5条Banner表记录，按权重weight升序", description = "无需token验证")
    @GetMapping("/list")
    @ResponseBody
    public Result list() {

        // 查询至少5条轮播图记录，按权重weight升序
        List<Banner> banners = bannerService.list();
        if (banners.isEmpty()) {
            log.warn("暂无轮播图记录，请联系运维人员");
            return Result.fail(0, "暂无轮播图记录，请联系运维人员");
        }

        return Result.ok(banners);
    }
}

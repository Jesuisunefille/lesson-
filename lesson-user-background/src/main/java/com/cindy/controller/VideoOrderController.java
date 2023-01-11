package com.cindy.controller;

import com.cindy.annotation.Token;
import com.cindy.param.VideoOrderDeleteParam;
import com.cindy.service.VideoOrderService;
import com.cindy.util.Result;
import com.cindy.vo.OrderPageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Cindy
 */
@Tag(name = "VideoOrderController", description = "视频订单中间表模块接口")
@RestController
@RequestMapping("/api/v1/video-order")
public class VideoOrderController {

    @Autowired
    private VideoOrderService videoOrderService;

    @Operation(summary = "按用户主键批查订单记录", description = "需要token验证")
    @Token
    @GetMapping("/page-detail-by-user-id")
    public Result pageDetailByUserId(@Parameter(description = "用户表主键")
                                     @RequestParam("user-id") Integer userId,
                                     @Parameter(description = "当前显示第几页")
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @Parameter(description = "每页显示多少条")
                                     @RequestParam(value = "size", defaultValue = "6") Integer size) {
        OrderPageVo orderPageVo = videoOrderService.pageDetailByUserId(userId, page, size);
        return orderPageVo.getTotal() > 0 ?
                Result.ok(orderPageVo) :
                Result.fail(0, "该用户暂无订单记录");
    }

    @Operation(summary = "按视频订单中间表主键单删记录", description = "需要token验证")
    @Token
    @PostMapping("/delete-by-video-order-id")
    public Result deleteByVideoOrderId(@RequestBody VideoOrderDeleteParam videoOrderDeleteParam) {
        return videoOrderService.deleteByVideoOrderId(videoOrderDeleteParam) > 0 ?
                Result.ok() :
                Result.fail(0, "删除失败");
    }
}

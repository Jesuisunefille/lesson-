package com.cindy.controller;

import com.cindy.annotation.Token;
import com.cindy.entity.VideoOrder;
import com.cindy.param.OrderDeleteParam;
import com.cindy.param.OrderInsertParam;
import com.cindy.service.OrderService;
import com.cindy.util.BindingResultUtil;
import com.cindy.util.Result;
import com.cindy.vo.OrderPageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cindy
 */
@Tag(name = "OrderController", description = "订单模块接口")
@Controller
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ResponseBody
    @Operation(summary = "添加订单", description = "需要token验证")
    @Token
    @PostMapping("/insert")
    public Result insert(@RequestBody @Validated OrderInsertParam orderInsertParam,
                         BindingResult bindingResult) {
        BindingResultUtil.check(bindingResult);
        return orderService.insert(orderInsertParam) > 0 ?
                Result.ok() :
                Result.fail(0, "下单失败");
    }

    @ResponseBody
    @Operation(summary = "根据用户主键查询订单记录", description = "需要token验证")
    @Token
    @GetMapping("/select-by-user-id")
    public Result selectByUserId(@RequestParam("user-id") @Parameter(description = "用户表主键") Integer userId) {
        List<VideoOrder> videoOrders = orderService.selectDetailByUserId(userId);
        return videoOrders.isEmpty() ?
                Result.fail(0, "该用户暂无订单记录") :
                Result.ok(videoOrders);
    }

    @ResponseBody
    @Operation(summary = "根据订单主键删除订单记录", description = "需要token验证")
    @Token
    @PostMapping("/delete-by-id")
    public Result deleteById(@RequestBody OrderDeleteParam orderDeleteParam) {
        return orderService.deleteById(orderDeleteParam) > 0 ?
                Result.ok() :
                Result.fail(0, "删除失败");
    }

    @ResponseBody
    @Operation(summary = "按用户主键批查订单记录", description = "需要token验证")
    @Token
    @GetMapping("/page-detail-by-user-id")
    public Result pageDetailByUserId(@Parameter(description = "用户表主键")
                                     @RequestParam("user-id") Integer userId,
                                     @Parameter(description = "当前显示第几页")
                                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @Parameter(description = "每页显示多少条")
                                     @RequestParam(value = "size", defaultValue = "6") Integer size) {
        OrderPageVo orderPageVo = orderService.pageDetailByUserId(userId, page, size);
        return orderPageVo.getTotal() > 0 ?
                Result.ok(orderPageVo) :
                Result.fail(0, "该用户暂无订单记录");
    }
}

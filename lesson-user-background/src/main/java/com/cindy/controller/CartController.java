package com.cindy.controller;

import com.cindy.annotation.Token;
import com.cindy.param.CartClearParam;
import com.cindy.param.CartDeleteParam;
import com.cindy.param.CartInsertParam;
import com.cindy.service.CartService;
import com.cindy.util.BindingResultUtil;
import com.cindy.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "CartController", description = "购物车模块接口")
@Controller
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "添加一条购物车信息", description = "需要token验证")
    @Token
    @PostMapping("/insert-or-update")
    @ResponseBody
    public Result insertOrUpdate(@RequestBody @Validated CartInsertParam cartInsertParam,
                                 BindingResult bindingResult) {
        BindingResultUtil.check(bindingResult);
        return cartService.insertOrUpdate(cartInsertParam) > -1 ?
                Result.ok() :
                Result.fail(0, "添加购物车失败");
    }

    @Operation(summary = "根据用户主键查询购物车记录", description = "需要token验证")
    @Token
    @GetMapping("/select-by-user-id")
    @ResponseBody
    public Result selectByUserId(@RequestParam("user-id") @Parameter(description = "用户表主键") Integer userId) {
        Map<String, String> result = cartService.selectByUserId(userId);
        return !result.isEmpty() ?
                Result.ok(result) :
                Result.fail(0, "该用户暂无购物车记录");
    }

    @Operation(summary = "根据用户主键和商品主键数组，批量删除该用户某些购物车信息", description = "需要token验证")
    @Token
    @PostMapping("/delete-by-user-id-and-video-ids")
    @ResponseBody
    public Result deleteByUserIdAndVideoIds(@RequestBody @Validated CartDeleteParam cartDeleteParam,
                                            BindingResult bindingResult) {
        BindingResultUtil.check(bindingResult);
        return cartService.deleteByUserIdAndVideoIds(cartDeleteParam) > 0 ?
                Result.ok() :
                Result.fail(0, "field不存在或删除失败");
    }

    @Operation(summary = "根据用户主键清空该用户全部购物车信息", description = "需要token验证")
    @Token
    @PostMapping("/delete-by-user-id")
    @ResponseBody
    public Result deleteByUserId(@RequestBody @Validated CartClearParam cartClearParam,
                                 BindingResult bindingResult) {
        BindingResultUtil.check(bindingResult);
        return cartService.deleteByUserId(cartClearParam) > -1 ?
                Result.ok() :
                Result.fail(0, "key不存在或清空失败");
    }
}

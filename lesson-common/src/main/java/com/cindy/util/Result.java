package com.cindy.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author cindy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应描述
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 操作成功，响应码为 `1`，描述为 `请求成功`，无响应数据。
     *
     * @return JsonResult对象
     */
    @SneakyThrows
    public static Result ok() {
        return new Result(1, "请求成功", null);
    }

    /**
     * 操作成功，响应码为 `1`，描述为 `请求成功`，响应数据自定义。
     *
     * @param data 响应数据
     * @return JsonResult对象
     */
    @SneakyThrows
    public static Result ok(Object data) {
        return new Result(1, "请求成功", data);
    }

    /**
     * 操作失败，响应码和描述自定义，无响应数据。
     *
     * @param code    响应状态码，正数表示成功，零或负数表示失败
     * @param message 响应失败描述
     * @return JsonResult对象
     */
    @SneakyThrows
    public static Result fail(Integer code, String message) {
        return new Result(code, message, null);
    }
}
package com.cindy.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;

/**
 * @author Cindy
 */
@Data
public class JacksonUtil {

    private static final String DATE_PATTERN = "yyyy/MM/dd hh:mm:ss";

    /**
     * 将响应数据格式化为JSON字符串，null值忽略，时间类型使用 `yyyy/MM/dd hh:mm:ss` 格式。
     *
     * @param data 响应数据
     * @return 格式化后的字符串
     */
    @SneakyThrows
    public static String format(Object data) {
        if (data == null) {
            throw new InvalidParameterException("参数包含null");
        }
        // 格式化时忽略null值
        // 格式化时的日期属性不使用时间戳
        // 格式化时的日期属性使用 `yyyy/MM/dd hh:mm:ss` 格式
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new SimpleDateFormat(DATE_PATTERN))
                .writeValueAsString(data);
    }

    /**
     * 将JSON字符串解析成指定类型并返回。
     *
     * @param json 符合JSON格式的字符串
     * @return 参数指定的类型
     */
    @SneakyThrows
    public static <T> T parse(String json, Class<T> c) {
        if (json == null || c == null) {
            throw new InvalidParameterException("参数包含null");
        }
        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat(DATE_PATTERN))
                .readValue(json, c);
    }
}
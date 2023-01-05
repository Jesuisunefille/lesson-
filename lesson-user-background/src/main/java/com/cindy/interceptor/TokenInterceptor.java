package com.cindy.interceptor;

import com.cindy.annotation.Token;
import com.cindy.util.JacksonUtil;
import com.cindy.util.JwtUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cindy
 * 前端拦截器调用时，若token快要过期，则获取后端新生成的token并存储起来
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final String EXPIRE_SOON_FLAG = "expireSoon";

    @SneakyThrows
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj) throws IOException {

        // 最终要返回的数据
        Map<String, Object> responseMap = new HashMap<>(3);

        // 若请求URL不指向controller方法，直接放行
        if (!(obj instanceof HandlerMethod)) {
            return true;
        }

        // 若请求URL指向的动作方法上没有标记@Token注解，直接放行
        Method method = ((HandlerMethod) obj).getMethod();
        if (!method.isAnnotationPresent(Token.class)) {
            return true;
        }

        // 从请求头中获取token，若没获取成功，尝试从请求参数中获取，若均失败返回错误提示
        String token = req.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = req.getParameter("token");
            if (StringUtils.isBlank(token)) {
                responseMap.put("code", 0);
                responseMap.put("message", "请求中未携带token或token过期");
                resp.setCharacterEncoding("UTF-8");
                resp.setContentType("application/json");
                PrintWriter writer = resp.getWriter();
                writer.write(JacksonUtil.format(responseMap));
                writer.close();
            }
        }

        // 解析token
        Map<String, Object> map = JwtUtil.verify(token);

        // 查看当前token是否即将过期，若是则返回过期提示
        if ((boolean) map.get(EXPIRE_SOON_FLAG)) {
            responseMap.put("code", 99);
            responseMap.put("message", "token即将在30分钟内过期，请及时更换");
            responseMap.put("data", map.get("newToken"));
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JacksonUtil.format(responseMap));
            writer.close();
        }

        // 放行请求
        return true;
    }
}
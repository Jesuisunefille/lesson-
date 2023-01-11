package com.cindy.config;

import com.cindy.entity.User;
import com.cindy.util.NullUtil;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Cindy
 */
@Aspect //以声明为切面类
@Configuration   //以声明为配置类
public class ServiceAspectConfig {

    /**
     * 异常通知建议丢给全局异常处理类来处理
     *
     * @param pjp 环绕通知参数
     * @return 业务方法返回值
     */
    @SneakyThrows
    @Around("execution(* com.cindy.service.impl..*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint pjp) {

        // 前置通知：若参数中存在null值，则直接抛出异常
        Object[] args = pjp.getArgs();
        if (NullUtil.hasNull(args)) {
            throw new NullPointerException("参数中存在null值..");
        }

        // 代理执行：通过反射调用业务方法
        Object returnValue = pjp.proceed(args);

        // 返回后通知：若返回值是User类型，则隐藏密码
        if (returnValue instanceof User) {
            User user = (User) returnValue;
            user.setPassword("");
            return user;
        }

        // 返回后通知：若返回值是List<User>类型，则循环隐藏密码
        if (returnValue instanceof List
                && ((List) returnValue).size() > 0
                && ((List) returnValue).get(0) instanceof User) {
            List<User> users = (List<User>) returnValue;  //隐藏密码
            users.forEach(user -> user.setPassword(""));
            return users;
        }

        // 后置通知：记录日志
        System.out.println("方法 {} 调用成功..");
        return returnValue;
    }
}
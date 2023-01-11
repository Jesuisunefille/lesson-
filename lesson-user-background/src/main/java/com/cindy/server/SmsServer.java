package com.cindy.server;

import com.cindy.util.SmsUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author Cindy
 */
@Slf4j
@Component
public class SmsServer {
    @SneakyThrows
    @Async
    public Future<String> sendOrderSms(String phone) {
        SmsUtil.sendSms(phone,"下单成功");
        return new AsyncResult<>("发送短信成功");
    }
}

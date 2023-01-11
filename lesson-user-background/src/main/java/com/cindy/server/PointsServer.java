package com.cindy.server;

import com.cindy.util.JedisStandaloneUtil;
import com.cindy.util.NullUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.concurrent.Future;

/**
 * @author Cindy
 */
@Slf4j
@Component
public class PointsServer {

    /**
     * <h2>为用户添加积分</h2>
     * <p> 01. 检查必要参数，若必要参数为空则直接抛出异常。
     * <p> 02. 使用 `JedisStandaloneUtil` 工具获取一个单机的Jedis连接实例。
     * <p> 03. 使用 `jedis.incrBy(key, points)` 向指定用户添加积分。
     * <p> 04. 调用 `JedisStandaloneUtil.closeJedis(jedis)` 关闭Jedis连接以节省资源。
     * <p> 05. 使用 `new AsyncResult<>("添加积分成功")` 构建返回值并返回，无返回值时参数传入null即可。
     *
     * @param userId   User主键
     * @param totalFee 订单总金额
     * @return Future类型的提示字符串
     */
    @SneakyThrows
    @Async
    public Future<String> incrByPoints(Integer userId, double totalFee) {
        if (NullUtil.hasNull(userId, totalFee)) {
            throw new RuntimeException("必要参数为空");
        }
        Jedis jedis = JedisStandaloneUtil.getJedis();
        String key = "points:user-" + userId;
        long points = (long) totalFee;
        jedis.incrBy(key, points);
        JedisStandaloneUtil.closeJedis(jedis);
        log.info("积分添加成功");
        return new AsyncResult<>("积分添加成功");
    }
}

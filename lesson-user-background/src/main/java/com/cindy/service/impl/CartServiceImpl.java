package com.cindy.service.impl;

import com.cindy.param.CartClearParam;
import com.cindy.param.CartDeleteParam;
import com.cindy.param.CartInsertParam;
import com.cindy.service.CartService;
import com.cindy.util.JacksonUtil;
import com.cindy.util.JedisStandaloneUtil;
import com.cindy.util.NullUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Map;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    private final String CART_KEY_PREFIX = "cart:user-";

    @Override
    public long insertOrUpdate(CartInsertParam cartInsertParam) {

        Integer userId = cartInsertParam.getUserId();
        Integer videoId = cartInsertParam.getVideoId();

        // 检查必要参数，若必要参数为空则直接抛出异常
        if (NullUtil.hasNull(userId, videoId,
                cartInsertParam.getTitle(),
                cartInsertParam.getAuthor(),
                cartInsertParam.getCoverImage(),
                cartInsertParam.getPrice())) {
            throw new RuntimeException("必要参数为空");
        }

        // 获取一个单机的Jedis连接实例
        Jedis jedis = JedisStandaloneUtil.getJedis();

        // 使用固定前缀如 `cart:user-` 拼接用户的主键字符串作为key值
        String key = CART_KEY_PREFIX + userId.toString();

        // 使用视频的主键字符串作为field值
        String field = videoId.toString();

        // 将整个 `cartInsertParam` 参数转换为JSON字符串作为value值
        String value = JacksonUtil.format(cartInsertParam);

        // 购物车记录的添加
        long result = jedis.hset(key, field, value);

        // 关闭jedis连接，节省资源
        JedisStandaloneUtil.closeJedis(jedis);

        // 返回影响条目数
        return result;
    }

    @Override
    public Map<String, String> selectByUserId(Integer userId) {

        // 获取一个单机的Jedis连接实例
        Jedis jedis = JedisStandaloneUtil.getJedis();

        // 使用固定前缀如 `cart:user-` 拼接用户的主键字符串作为key值
        String key = CART_KEY_PREFIX + userId.toString();

        // 查询指定用户的全部购物车记录
        Map<String, String> result = jedis.hgetAll(key);

        // 关闭Jedis连接以节省资源
        JedisStandaloneUtil.closeJedis(jedis);

        // 返回影响条目数
        return result;
    }

    @Override
    public long deleteByUserIdAndVideoIds(CartDeleteParam cartDeleteParam) {

        long deleteResult = 0L;
        Integer userId = cartDeleteParam.getUserId();
        Integer[] videoIds = cartDeleteParam.getVideoIds();

        // 检查必要参数
        if (NullUtil.hasNull(userId, videoIds)) {
            throw new RuntimeException("必要参数为空");
        }

        // 获取一个单机的Jedis连接实例
        Jedis jedis = JedisStandaloneUtil.getJedis();

        // 使用固定前缀如 `cart:user-` 拼接用户的主键字符串作为key值
        String key = CART_KEY_PREFIX + userId;

        for (Integer videoId : videoIds) {

            // 使用视频的主键字符串作为field值
            String field = videoId.toString();
            if (jedis.hexists(key, field)) {
                deleteResult += jedis.hdel(key, field);
            }
        }

        // 关闭Jedis连接以节省资源
        JedisStandaloneUtil.closeJedis(jedis);

        // 返回影响条目数
        return deleteResult;
    }

    @Override
    public long deleteByUserId(CartClearParam cartClearParam) {

        long result = 0L;

        // 检查必要参数，若必要参数为空则直接抛出异常。
        Integer userId = cartClearParam.getUserId();
        if (null == userId) {
            throw new RuntimeException("必要参数为空");
        }

        // 使用固定前缀如 `cart:user-` 拼接用户的主键字符串作为key值
        String key = CART_KEY_PREFIX + userId;

        // 获取一个单机的Jedis连接实例
        Jedis jedis = JedisStandaloneUtil.getJedis();

        // 删除指定用户的全部购物车记录
        if (jedis.exists(key)) {
            result = jedis.del(key);
        }

        // 关闭Jedis连接以节省资源
        JedisStandaloneUtil.closeJedis(jedis);

        // 返回影响条目数
        return result;
    }
}

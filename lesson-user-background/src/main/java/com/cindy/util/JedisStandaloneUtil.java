package com.cindy.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author cindy
 */

public class JedisStandaloneUtil {

    /**
     * 单机模式下的Jedis连接池
     */
    private static JedisPool jedisStandalonePool;

    /**
     * redis单机服务端IP地址
     */
    private static String host = "192.168.242.77";

    /**
     * redis单机服务端端口号
     */
    private static Integer port = 6379;

    /**
     * redis单机服务端连接超时毫秒数
     */
    private static Integer timeout = 10000;

    static {

        /* 创建一个Jedis连接池的配置实例
         * 最多容纳1024个连接
         * 最大等待时间10s
         * 最大空闲200
         * 最小空闲0
         * 连接耗尽时阻塞直到超时*/
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxWaitMillis(10000L);
        jedisPoolConfig.setMaxIdle(200);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setBlockWhenExhausted(true);

        // 创建Jedis单机连接池
        jedisStandalonePool = new JedisPool(jedisPoolConfig, host, port, timeout);
    }

    /**
     * 获取一个Jedis单机连接实例，若连接无法连通redis服务端则抛出异常
     *
     * @return 一个Jedis单机连接实例
     */
    public static Jedis getJedis() {
        Jedis jedis = jedisStandalonePool.getResource();
        if (jedis == null || !("PONG".equals(jedis.ping()))) {
            throw new RuntimeException("Jedis为空或Jedis连接redis服务端失败");
        }
        return jedis;
    }

    /**
     * 关闭一个Jedis连接
     *
     * @param jedis Jedis连接
     */
    public static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}

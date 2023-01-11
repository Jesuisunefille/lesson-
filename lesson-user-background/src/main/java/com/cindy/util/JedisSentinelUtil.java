package com.cindy.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Cindy
 */

public class JedisSentinelUtil {

    /**
     * 哨兵模式下的Jedis连接池
     */
    private static JedisSentinelPool jedisSentinelPool;

    /**
     * 全部哨兵节点信息，格式为 "IP:PORT;IP:PORT ..."
     */
    private static String sentinelNodes = "127.0.0.1:7007;127.0.0.1:7008;127.0.0.1:7009";

    /**
     * 哨兵节点所监控的主从结构名称
     */
    private static String sentinelMasterNodes = "m7007";

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

        // 创建Jedis哨兵连接池
        Set<String> sentinels = new HashSet<>(Arrays.asList(sentinelNodes.split(";")));
        jedisSentinelPool = new JedisSentinelPool(sentinelMasterNodes, sentinels, jedisPoolConfig);
    }



    /**
     * 获取一个Jedis哨兵连接实例，若连接无法连通redis服务端则抛出异常
     */
    public static Jedis getJedis() {
        Jedis jedis = jedisSentinelPool.getResource();
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

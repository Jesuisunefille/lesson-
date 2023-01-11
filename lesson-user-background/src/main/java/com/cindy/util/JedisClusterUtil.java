package com.cindy.util;

import lombok.SneakyThrows;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Cindy
 */

public class JedisClusterUtil {

    /**
     * 集群模式下的Jedis连接
     */
    private static JedisCluster jedisCluster;

    /**
     * 全部集群节点信息，格式为 "IP:PORT;IP:PORT ..."
     */
    private static String clusterNodes = "127.0.0.1:7011;127.0.0.1:7012;127.0.0.1:7013;127.0.0.1:7014;127.0.0.1:7015;127.0.0.1:7016";

    /**
     * redis集群服务端连接超时毫秒数
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

        // 创建Jedis集群连接池
        Set<HostAndPort> nodes = new HashSet<>();
        for (String clusterNode : clusterNodes.split(";")) {
            String[] hostAndPort = clusterNode.split(":");
            nodes.add(new HostAndPort(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
        jedisCluster = new JedisCluster(nodes, timeout, jedisPoolConfig);
    }

    /**
     * 获取一个Jedis集群连接，若集群中任意一个连接无法连通服redis务端则抛出异常
     *
     * @return Jedis集群连接
     */
    public static JedisCluster getJedis() {

        // 获取集群中所有的节点
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();

        // 遍历所有节点
        for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
            // 获取当前节点的一个Jedis连接
            Jedis jedis = entry.getValue().getResource();
            if (!"PONG".equals(jedis.ping())) {
                throw new RuntimeException("连接redis服务端失败...");
            }
            // 释放资源
            jedis.close();
        }

        return jedisCluster;
    }

    /**
     * 关闭一个JedisCluster连接
     *
     * @param jedisCluster JedisCluster连接
     */
    @SneakyThrows
    public static void closeJedisCluster(JedisCluster jedisCluster) {
        if (jedisCluster != null) {
            jedisCluster.close();
        }
    }
}

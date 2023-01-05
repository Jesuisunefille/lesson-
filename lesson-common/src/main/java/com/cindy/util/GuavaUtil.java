package com.cindy.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author cindy
 */
public class GuavaUtil {

    public static Cache<String, Object> getTenMinCache() {
        return CacheHolder.TEN_MIN_CACHE;
    }

    public static Cache<String, Object> getHalfHourCache() {
        return CacheHolder.HALF_HOUR_CACHE;
    }

    /**
     * 用于构建单例模式的缓存实例
     */
    private static final class CacheHolder {

        /**
         * 10分钟缓存实例：设置缓存初始值，最大值，并发数，过期时间，并统计缓存命中率
         */
        static final Cache<String, Object> TEN_MIN_CACHE = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .concurrencyLevel(5)
                .expireAfterWrite(600, TimeUnit.SECONDS)
                .recordStats()
                .build();

        /**
         * 30分钟缓存实例：设置缓存初始值，最大值，并发数，过期时间，并统计缓存命中率
         */
        static final Cache<String, Object> HALF_HOUR_CACHE = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(100)
                .concurrencyLevel(5)
                .expireAfterWrite(1800, TimeUnit.SECONDS)
                .recordStats()
                .build();
    }
}

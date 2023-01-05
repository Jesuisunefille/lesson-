package com.cindy.util;

import java.util.BitSet;

/**
 * @author cindy
 */
public class BloomFilterUtil {


    /**
     * 根据这个数组来创建多个hash函数
     **/
    private static final int[] SEEDS = new int[]{3, 13, 46, 71, 91, 134};

    /**
     * 位组
     **/
    private static final BitSet BIT_SET = new BitSet(2 << 24);

    /**
     * hash函数数组
     **/
    private static final BloomFunction[] BLOOM_FUNCTIONS = new BloomFunction[SEEDS.length];

    static {

        // 初始化6个Bloom函数
        for (int i = 0; i < SEEDS.length; ++i) {
            BLOOM_FUNCTIONS[i] = new BloomFunction(SEEDS.length, SEEDS[i]);
        }
    }

    /**
     * 将一个元素添加到布隆过滤器中
     *
     * @param value: 元素
     **/
    public static void add(Object value) {
        // 分别计算value的hash值然后将对应的位置true
        for (BloomFunction bloomFunction : BLOOM_FUNCTIONS) {
            BIT_SET.set(bloomFunction.hash(value), true);
        }
    }

    /**
     * 判断一个元素是否在布隆过滤器中
     *
     * @param value: 元素
     **/
    public static boolean isContain(Object value) {
        boolean res = true;
        // 计算全部的hash值，看看是否全为true
        for (BloomFunction bloomFunction : BLOOM_FUNCTIONS) {
            res &= BIT_SET.get(bloomFunction.hash(value));
        }
        return res;
    }

    /**
     * 静态内部类，用于实现hash
     **/
    public static class BloomFunction {

        private final int capacity;
        private final int seed;

        public BloomFunction(int capacity, int seed) {
            this.capacity = capacity;
            this.seed = seed;
        }

        /**
         * hash函数
         * 仿照 HashMap中的hash函数：
         * return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
         *
         * @param value: 需要进行hash的值
         * @return int 返回hash计算出来的值
         **/
        public int hash(Object value) {
            int h;
            return (value == null) ? 0 : Math.abs(seed * (capacity - 1) & ((h = value.hashCode()) ^ (h >>> 16)));
        }
    }
}

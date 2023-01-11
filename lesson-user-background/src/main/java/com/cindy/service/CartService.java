package com.cindy.service;


import com.cindy.param.CartClearParam;
import com.cindy.param.CartDeleteParam;
import com.cindy.param.CartInsertParam;

import java.util.Map;

/**
 * @author Cindy
 */
public interface CartService {

    /**
     * <h2>向Redis中添加或修改一条购物车信息</h2>
     * <p> 01. 检查必要参数，若必要参数为空则直接抛出异常。
     * <p> 02. 使用 `JedisStandaloneUtil` 工具获取一个单机的Jedis连接实例。
     * <p> 03. 组装Key值：使用固定前缀如 `cart:user-` 拼接用户的主键字符串作为key值。
     * <p> 04. 组装Field值：使用视频的主键字符串作为field值。
     * <p> 05. 组装Value值：将整个 `cartInsertParam` 参数转换为JSON字符串作为value值。
     * <p> 06. 调用 `jedis.hset(key, field, value)` 完成购物车记录的添加。
     * <p> 07. 调用 `JedisStandaloneUtil.closeJedis(jedis)` 关闭Jedis连接以节省资源。
     *
     * @param cartInsertParam 添加购物车Param实体类
     * @return 影响条目数：添加成功返回1，修改成功返回0，失败返回负数
     */
    long insertOrUpdate(CartInsertParam cartInsertParam);

    /**
     * <h2>根据用户主键，返回该用户全部的购物车信息</h2>
     * <p> 01. 使用 `JedisStandaloneUtil` 工具获取一个单机的Jedis连接实例。
     * <p> 02. 组装Key值：使用固定前缀如 `cart:user-` 拼接用户的主键字符串作为k
     * <p> 03. 调用 `jedis.hgetAll(key)` 查询指定用户的全部购物车记录。
     * <p> 04. 调用 `JedisStandaloneUtil.closeJedis(jedis)` 关闭Jedis连接以节省资源。
     *
     * @param userId 用户主键
     * @return 全部的购物车信息，若key不存在则返回空map
     */
    Map<String, String> selectByUserId(Integer userId);

    /**
     * <h2>根据用户主键和商品主键，删除该用户某条购物车信息</h2>
     * <p> 01. 检查必要参数，若必要参数为空则直接抛出异常。
     * <p> 02. 使用 `JedisStandaloneUtil` 工具获取一个单机的Jedis连接实例。
     * <p> 03. 组装Key值：使用固定前缀如 `cart:user-` 拼接用户的主键字符串作为key值。
     * <p> 04. 循环组装Field值：使用视频的主键字符串作为field值。
     * <p> 05. 循环调用 `jedis.hdel(key, field)` 查询指定用户和指定视频的的购物车记录。
     * <p> 06. 调用 `JedisStandaloneUtil.closeJedis(jedis)` 关闭Jedis连接以节省资源。
     *
     * @param cartDeleteParam 删除购物车Param实体类
     * @return 影响条目数：key或field不存在时，均会返回-1
     */
    long deleteByUserIdAndVideoIds(CartDeleteParam cartDeleteParam);

    /**
     * <h2>清空购物车：根据用户主键清空该用户全部购物车信息</h2>
     * <p> 01. 检查必要参数，若必要参数为空则直接抛出异常。
     * <p> 02. 使用 `JedisStandaloneUtil` 工具获取一个单机的Jedis连接实例。
     * <p> 03. 组装Key值：使用固定前缀如 `cart:user-` 拼接用户的主键字符串作为key值。
     * <p> 04. 循环调用 `jedis.del(key)` 查询指定用户的全部购物车记录。
     * <p> 05. 调用 `JedisStandaloneUtil.closeJedis(jedis)` 关闭Jedis连接以节省资源。
     * @param cartClearParam 清空购物车的Param实体类
     * @return 影响条目数
     */
    long deleteByUserId(CartClearParam cartClearParam);

}

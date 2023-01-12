package com.cindy.service;

import com.cindy.entity.VideoOrder;
import com.cindy.param.OrderDeleteParam;
import com.cindy.param.OrderInsertParam;
import com.cindy.vo.OrderPageVo;

import java.util.List;

/**
 * @author Cindy
 */
public interface OrderService {

    /**
     * <h2>下单购买视频</h2>
     * <p> 01. 添加 `@Transactional(rollbackFor = Exception.class)` 本地事务保护，抛出异常时触发回滚。
     * <p> 02. 检查必填参数：若包含null值则直接抛出参数异常。
     * <p> 03. 根据userId检查下单用户是否存在，不存在抛异常，存在则返回该条User表记录。
     * <p> 04. 根据videoIds检查要购买的视频是否全部存在，有一个不存在就抛出异常。
     * <p> 05. 根据userId和videoIds检查用户是否重复购买视频，重复购买则抛异常。
     * <p> 06. 根据totalFee订单总价属性，生成一条订单号随机，状态为1的Order表记录。
     * <p> 07. 调用Mapper接口添加Order表记录，添加失败抛异常。
     * <p> 08. 调用Mapper接口循环添加VideoOrder表记录，添加失败抛异常。
     * <p> 09. 下单整体流程结束，返回1
     *
     * @param orderInsertParam 用户下单购买视频业务实体参数
     * @return 影响条目数
     */
    int insert(OrderInsertParam orderInsertParam);

    /**
     * <h2>查询个人订单</h2>
     * <p> 01. 检查必填参数：若包含null值则直接抛出参数异常。
     * <p> 02. 调用Mapper接口检查用户是否存在，若不存在抛出异常。
     * <p> 03. 调用Mapper接口按用户主键查询该用户的全部完整VideoOrder记录。
     * <p> 04. 查询成功：返回该用户的全部完整VideoOrder记录。
     * <p> 05. 查询失败：直接返回null值。
     *
     * @param userId User表主键
     * @return 指定用户的全部订单
     */
    List<VideoOrder> selectDetailByUserId(Integer userId);

    /**
     * <h2>删除个人订单</h2>
     * <p> 01. 添加 `@Transactional(rollbackFor = Exception.class)` 本地事务保护，抛出异常时触发回滚。
     * <p> 02. 检查必填参数：若包含null值则直接抛出参数异常。
     * <p> 03. 调用Mapper接口检查订单是否存在，若不存在抛出异常。
     * <p> 04. 调用Mapper接口删除该订单的所有VideoOrder表记录。
     * <p> 05. 调用Mapper接口删除该订单的Order记录。
     *
     * @param orderDeleteParam 删除订单的Param实体
     * @return 影响条目数
     */
    int deleteById(OrderDeleteParam orderDeleteParam);


    /**
     * <h2>分页查询个人订单详情</h2>
     * <p> 01. 调用Mapper接口检查用户是否存在，若不存在抛出异常。
     * <p> 02. 调用Mapper接口按用户主键查询该用户的全部完整VideoOrder记录。
     * <p> 03. 查询成功：返回该用户的全部完整VideoOrder记录。
     * <p> 04. 查询失败：直接返回null值。
     *
     * @param userId User主键
     * @param page   当前显示第几页
     * @param size   每页显示多少条
     * @return 指定用户的部分订单数据的VO实体
     */
    OrderPageVo pageDetailByUserId(Integer userId, Integer page, Integer size);



    /**
     * <h2>删除个人订单</h2>
     * <p> 01. 添加 `@Transactional(rollbackFor = Exception.class)` 本地事务保护，抛出异常时触发回滚。
     * <p> 02. 检查必填参数：若包含null值则直接抛出参数异常。
     * <p> 03. 调用Mapper接口检查订单是否存在，若不存在抛出异常。
     * <p> 04. 调用Mapper接口删除该订单的所有VideoOrder表记录。
     * <p> 05. 调用Mapper接口删除该订单的Order记录。
     *
     * @param orderDeleteParam 删除订单的Param实体
     * @return 影响条目数
     */
    int deleteByOrderId(OrderDeleteParam orderDeleteParam);
}

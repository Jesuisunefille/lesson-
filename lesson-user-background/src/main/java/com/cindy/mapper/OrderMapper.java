package com.cindy.mapper;

import com.cindy.entity.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author Cindy
 */
@Repository
public interface OrderMapper {
    String SELECT_ALL = "SELECT o.id, o.number, o.state, o.total_fee, o.info, o.create_time, o.last_modify " +
            "FROM lesson.`order` o ";

    /**
     * 单增Order记录
     *
     * @param order 订单实体
     * @return 影响条目数
     */
    @Insert("INSERT INTO lesson.`order` (number, state, total_fee, info, create_time, last_modify) " +
            "VALUES (#{number}, #{state}, #{totalFee}, #{info}, #{createTime}, #{lastModify})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    /**
     * 按主键单查Order记录
     *
     * @param id Order表主键
     * @return 单条Order记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> o.id = #{param1} </if> " +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    Order selectById(Integer id);

    /**
     * 按主键单删Order记录
     *
     * @param id Order表主键
     * @return 影响条目数
     */
    @Delete("<script>" +
            "DELETE FROM lesson.`order`" +
            "<where>" +
            "<if test='_parameter != null'> id = #{param1} </if> " +
            "OR 1 = 2 " +
            "</where>" +
            "</script>")
    int deleteById(Integer id);


    /**
     * 按订单主键单删Order记录
     *
     * @param orderId Order表主键
     * @return 影响条目数
     */
    @Delete("<script>" +
            "DELETE FROM lesson.`order`" +
            "<where>" +
            "<if test='_parameter != null'> id = #{param1} </if> " +
            "OR 1 = 2 " +
            "</where>" +
            "</script>")
    int deleteByOrderId(Integer orderId);


    /**
     * 按订单主键单查Order记录
     *
     * @param orderId Order表主键
     * @return 单条Order记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> o.id = #{param1} </if> " +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    Order selectByOrderId(Integer orderId);
}

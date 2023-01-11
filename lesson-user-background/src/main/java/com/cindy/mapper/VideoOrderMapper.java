package com.cindy.mapper;

import com.cindy.entity.Order;
import com.cindy.entity.User;
import com.cindy.entity.Video;
import com.cindy.entity.VideoOrder;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Cindy
 */
@Repository
public interface VideoOrderMapper {

    String SELECT_ALL = "SELECT vo.id, vo.order_id, vo.video_id, vo.user_id, vo.info, vo.create_time, vo.last_modify " +
            "FROM lesson.video_order vo ";

    /**
     * 单增VideoOrder记录
     *
     * @param videoOrder VideoOrder实体
     * @return 影响条目数
     */
    @Insert("INSERT INTO lesson.video_order (order_id, video_id, user_id, info, create_time, last_modify) " +
            "VALUES (#{orderId}, #{videoId}, #{userId}, #{info}, #{createTime}, #{lastModify})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VideoOrder videoOrder);

    /**
     * 按User主键单删VideoOrder记录
     *
     * @param userId User主键
     * @return 影响条目数
     */
    @Delete("<script>" +
            "DELETE FROM lesson.video_order " +
            "<where>" +
            "<if test='_parameter != null'> user_id = #{param1} </if> OR 1 = 2 </where>" +
            "</script>")
    int deleteByUserId(Integer userId);

    /**
     * 按主键查询VideoOrder表记录，分步查询Video表，Order表和User表
     *
     * @param id VideoOrder主键
     * @return 全部VideoOrder记录
     */
    @Results(id = "video_order_detail", value = {
            @Result(id = true, column = "id", property = "id", jdbcType = JdbcType.INTEGER, javaType = Integer.class),
            @Result(column = "order_id", property = "order", jdbcType = JdbcType.INTEGER, javaType = Order.class,
                    one = @One(select = "com.cindy.mapper.OrderMapper.selectById")),
            @Result(column = "video_id", property = "video", jdbcType = JdbcType.INTEGER, javaType = Video.class,
                    one = @One(select = "com.cindy.mapper.VideoMapper.selectById")),
            @Result(column = "user_id", property = "user", jdbcType = JdbcType.INTEGER, javaType = User.class,
                    one = @One(select = "com.cindy.mapper.UserMapper.selectById"))
    })
    @Select("<script> " + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> vo.id = #{param1} </if> OR 1 = 2 </where>" +
            "</script>")
    VideoOrder selectDetailById(Integer id);

    /**
     * 按User主键查询VideoOrder记录
     *
     * @param userId User主键
     * @return 全部VideoOrder记录
     */
    @Select("<script> " + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> vo.user_id = #{param1} </if> OR 1 = 2 </where>" +
            "</script>")
    List<VideoOrder> selectByUserId(Integer userId);

    /**
     * 按User主键查询VideoOrder记录，分步查询Video表，Order表和User表
     *
     * @param userId User主键
     * @return 全部VideoOrder记录
     */
    @ResultMap("video_order_detail")
    @Select("<script> " + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> vo.user_id = #{param1} </if> OR 1 = 2 </where>" +
            "</script>")
    List<VideoOrder> selectDetailByUserId(Integer userId);


    /**
     * 按用户主键和视频主键数组查询VideoOrder记录
     *
     * @param userId 用户主键
     * @param videoIds 视频主键数组
     * @return 批量VideoOrder记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='arg0 != null and arg1 != null'>" +
            "vo.user_id = #{param1} and " +
            "<foreach collection='arg1' item='e' open='vo.video_id in (' close=') ' separator=',' > #{e} </foreach>" +
            "</if>" +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    List<Video> selectByUserIdAndVideoIds(Integer userId, Integer[] videoIds);

    /**
     * 按VideoOrder主键单删VideoOrder记录
     *
     * @param videoOrderId Order主键
     * @return 影响条目数
     */
    @Delete("<script>" +
            "DELETE FROM lesson.video_order " +
            "<where>" +
            "<if test='_parameter != null'> id = #{param1} </if> OR 1 = 2 </where>" +
            "</script>")
    int deleteByVideoOrderId(Integer videoOrderId);

    /**
     * 按Order主键查询VideoOrder记录
     *
     * @param orderId Order主键
     * @return 全部VideoOrder记录
     */
    @Select("<script> " + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> vo.order_id = #{param1} </if> OR 1 = 2 </where>" +
            "</script>")
    List<VideoOrder> selectByOrderId(Integer orderId);


    /**
     * 按Order主键单删VideoOrder记录
     *
     * @param orderId Order主键
     * @return 影响条目数
     */
    @Delete("<script>" +
            "DELETE FROM lesson.video_order " +
            "<where>" +
            "<if test='_parameter != null'> order_id = #{param1} </if> OR 1 = 2 </where>" +
            "</script>")
    int deleteByOrderId(Integer orderId);

}

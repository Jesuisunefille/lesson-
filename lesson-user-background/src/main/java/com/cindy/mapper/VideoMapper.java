package com.cindy.mapper;

import com.cindy.entity.Video;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Cindy
 */
@Repository
public interface VideoMapper {

    String SELECT_ALL = "SELECT v.id, v.title, v.author, v.info, v.summary_image, v.cover_image, " +
            "v.price, v.star, v.create_time, v.last_modify " +
            "FROM lesson.video v ";

    /**
     * 全查Video记录
     *
     * @return 全部Video记录
     */
    @Select(SELECT_ALL)
    List<Video> list();

    /**
     * 按主键单查Video记录
     *
     * @param id Video表主键
     * @return 单条Video记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> v.id = #{param1} </if> " +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    Video selectById(Integer id);

    /**
     * 按主键批查Video记录
     *
     * @param ids 主键数组
     * @return 批量Video记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'>" +
            "<foreach collection='array' item='e' open='id in (' close=')' separator=',' > #{e} </foreach>" +
            "</if>" +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    List<Video> selectByIds(Integer[] ids);

    /**
     * 按主键单查Video完整记录：分步查询视频中章的信息和每一张下集的信息
     *
     * @param id Video表主键
     * @return 单条Video完整记录
     */
    @Results({
            @Result(id = true, column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "id", property = "chapters", jdbcType = JdbcType.INTEGER, javaType = List.class,
                    many = @Many(select = "com.cindy.mapper.ChapterMapper.selectDetailByVideoId")
            )
    })
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> v.id = #{param1} </if> " +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    Video selectDetailById(Integer id);

    /**
     * 根据视频标题title模糊查询匹配的Video记录
     *
     * @param title 视频标题
     * @return 匹配的Video记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> v.title like concat('%', #{param1}, '%' )</if> " +
            "OR 1 = 2" +
            "</where>" +
            "</script>")
    List<Video> selectLikeTitle(String title);
}

package com.cindy.mapper;

import com.cindy.entity.Chapter;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterMapper {

    String SELECT_ALL = "SELECT c.id, c.title, c.index_in_video, c.video_id, c.info, c.create_time, c.last_modify " +
            "FROM lesson.chapter c ";

    /**
     * 按video主键查询全部Chapter记录，分步查询每个Chapter下的所有Episode记录，按index_in_video升序
     *
     * @param videoId Video主键
     * @return 全部Chapter记录，包括每个Chapter下的所有Episode记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> video_id = #{param1} </if>" +
            "or 1 = 2" +
            "</where> order by index_in_video" +
            "</script>")
    @Results({
            @Result(id = true, column = "id", property = "id", jdbcType = JdbcType.INTEGER, javaType = Integer.class),
            @Result(column = "id", property = "episodes", javaType = List.class,
                    many = @Many(select = "com.cindy.mapper.EpisodeMapper.selectByChapterId")
            )
    })
    List<Chapter> selectDetailByVideoId(Integer videoId);

    /**
     * 按video主键查询第一条Chapter记录
     *
     * @param videoId Video主键
     * @return 第一条Chapter记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> video_id = #{param1} and index_in_video = 1 </if>" +
            "or 1 = 2" +
            "</where>" +
            "</script>")
    Chapter selectFirstByVideoId(Integer videoId);
}

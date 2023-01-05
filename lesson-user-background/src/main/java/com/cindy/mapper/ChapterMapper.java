package com.cindy.mapper;

import com.cindy.entity.Chapter;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterMapper {
    String SELECT_ALL = "SELECT c.id, c.title, c.index_in_video, c.video_id, c.info, c.create_time, c.last_modify " +
            "FROM lesson.chapter c ";

    /**
     * 通过video表主键查询全部Chapter记录：分步查询每个Chapter下的所有Episode记录
     *
     * @param videoId video表主键
     * @return 该video下的所有章记录
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
}

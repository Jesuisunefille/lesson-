package com.cindy.mapper;

import com.cindy.entity.Episode;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeMapper {
    String SELECT_ALL = "SELECT e.id, e.title, e.cover_image, e.url, " +
            "e.index_in_chapter, e.chapter_id, e.info, e.create_time, e.last_modify " +
            "FROM lesson.episode e ";

    /**
     * 按Chapter表主键查询全部Episode记录
     *
     * @param chapterId Chapter表主键
     * @return 全部符合条件的的Episode记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> and e.chapter_id = #{param1} </if> or 1 = 2" +
            "</where> order by index_in_chapter" +
            "</script>")
    List<Episode> selectByChapterId(Integer chapterId);
}

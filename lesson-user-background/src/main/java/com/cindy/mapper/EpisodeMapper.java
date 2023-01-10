package com.cindy.mapper;

import com.cindy.entity.Episode;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Cindy
 */
@Repository
public interface EpisodeMapper {

    String SELECT_ALL = "SELECT e.id, e.title, e.cover_image, e.url, " +
            "e.index_in_chapter, e.chapter_id, e.info, e.create_time, e.last_modify " +
            "FROM lesson.episode e ";

    /**
     * 按Chapter主键查询全部Episode记录，按index_in_chapter升序
     *
     * @param chapterId Chapter主键
     * @return 全部Episode记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> and e.chapter_id = #{param1} </if> or 1 = 2" +
            "</where> order by index_in_chapter" +
            "</script>")
    List<Episode> selectByChapterId(Integer chapterId);

    /**
     * 按Video主键查询第一条Episode记录
     * @param videoId Video主键
     * @return 该视频下的第一条Episode记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> " +
            "and e.chapter_id = ( select c.id from lesson.chapter c where c.video_id = #{param1} and c.index_in_video = 1 ) " +
            "and e.index_in_chapter = 1 " +
            "</if> " +
            "or 1 = 2" +
            "</where>" +
            "</script>")
    Episode selectFirstByVideoId(Integer videoId);

    /**
     * 按Chapter主键查询第一条Episode记录
     *
     * @param chapterId Chapter主键
     * @return 第一条Episode记录
     */
    @Select("<script>" + SELECT_ALL +
            "<where>" +
            "<if test='_parameter != null'> and e.chapter_id = #{param1} and index_in_chapter = 1 </if> or 1 = 2" +
            "</where>" +
            "</script>")
    Episode selectFirstByChapterId(Integer chapterId);
}

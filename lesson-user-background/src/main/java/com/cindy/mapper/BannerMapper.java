package com.cindy.mapper;

import com.cindy.entity.Banner;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerMapper {
    String SELECT_ALL = "SELECT b.id, b.url, b.src, b.weight, b.info, b.create_time, b.last_modify " +
            "FROM lesson.banner b ";

    /**
     * 查询最多5条Banner记录并按weight升序
     * @return 最多5条Banner表记录
     */
    @Select(SELECT_ALL + "ORDER BY b.weight LIMIT 5")
    @Options(timeout = 1000, useCache = false)
    List<Banner> list();
}

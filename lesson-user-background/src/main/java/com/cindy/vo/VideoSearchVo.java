package com.cindy.vo;

import com.cindy.entity.Video;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Schema(name = "VideoSearchVo", description = "视频搜索分页VO实体")
@Data
@JsonIgnoreProperties("handler")
public class VideoSearchVo implements Serializable {

    private Long total;

    @JsonProperty("page-num")
    private Integer pageNum;

    @JsonProperty("page-size")
    private Integer pageSize;

    @JsonProperty("videos")
    private List<Video> videos;
}

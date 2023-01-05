package com.cindy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("handler")
public class Episode implements Serializable {

    private Integer id;
    private String title;
    private String url;
    private String info;

    @JsonProperty("cover-image")
    private String coverImage;

    @JsonProperty("index-in-chapter")
    private Integer indexInChapter;

    @JsonProperty("chapter-id")
    private Integer chapterId;

    @JsonProperty("create-time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createTime;

    @JsonProperty("last-modify")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date lastModify;
}

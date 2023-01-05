package com.cindy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("handler")
public class Video implements Serializable {
    private Integer id;
    private String title;
    private String author;
    private String info;
    private Double price;
    private Integer star;

    @JsonProperty("summary-image")
    private String summaryImage;

    @JsonProperty("cover-image")
    private String coverImage;

    @JsonProperty("create-time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createTime;

    @JsonProperty("last-modify")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date lastModify;

    /**
     * 每个视频拥有多个章，1:N
     */
    private List<Chapter> chapters;
}

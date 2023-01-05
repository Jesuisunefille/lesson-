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
public class VideoOrder implements Serializable {

    private Integer id;
    private String info;

    @JsonProperty("video-id")
    private Integer videoId;

    @JsonProperty("order-id")
    private Integer orderId;

    @JsonProperty("user-id")
    private Integer userId;

    @JsonProperty("create-time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createTime;

    @JsonProperty("last-modify")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date lastModify;

    /**
     * 每条中间表记录对应一个视频，1:1
     */
    private Video video;

    /**
     * 每条中间表记录对应一个订单，1:1
     */
    private Order order;

    /**
     * 每条中间表记录对应一个用户，1:1
     */
    private User user;
}

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
public class Order implements Serializable {

    private Integer id;
    private String number;
    private Integer state;
    private String info;

    @JsonProperty("total-fee")
    private Double totalFee;

    @JsonProperty("create-time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createTime;

    @JsonProperty("last-modify")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date lastModify;
}

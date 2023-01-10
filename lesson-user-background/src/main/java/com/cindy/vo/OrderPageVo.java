package com.cindy.vo;

import com.cindy.entity.VideoOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * @author cindy
 */
@Schema(name = "OrderPageVo", description = "订单分页VO实体")
@Data
@JsonIgnoreProperties("handler")
public class OrderPageVo implements Serializable {

    private Integer pages;
    private Long total;

    @JsonProperty("page-num")
    private Integer pageNum;

    @JsonProperty("page-size")
    private Integer pageSize;

    @JsonProperty("video-orders")
    private List<VideoOrder> videoOrders;
}

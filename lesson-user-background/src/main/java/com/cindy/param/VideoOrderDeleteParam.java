package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Cindy
 */
@Schema(name = "VideoOrderDeleteParam", description = "用于删除中间表的Param实体参数")
@Data
public class VideoOrderDeleteParam implements Serializable {

    @Schema(description = "订单主键", required = true, example = "1")
    @NotNull(message = "删除订单业务中的订单主键不能为空")
    @JsonProperty("order-id")
    private Integer orderId;

    @Schema(description = "中间表主键", required = true, example = "1")
    @NotNull(message = "删除中间表业务中的中间表主键不能为空")
    @JsonProperty("video-order-id")
    private Integer videoOrderId;
}

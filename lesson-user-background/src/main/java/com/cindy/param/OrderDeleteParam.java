package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Cindy
 */
@Schema(name = "OrderDeleteParam", description = "用于删除订单的Param实体参数")
@Data
public class OrderDeleteParam implements Serializable {

    @Schema(description = "订单主键", required = true, example = "1")
    @NotNull(message = "删除订单业务中的订单主键不能为空")
    @JsonProperty("order-id")
    private Integer orderId;
}

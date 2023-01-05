package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author JoeZhou
 */
@Schema(name = "OrderInsertParam", description = "用于用户下单购买视频的Param实体参数")
@Data
public class OrderInsertParam implements Serializable {

    @Schema(description  = "视频主键数组", required = true, example = "[1,2]")
    @NotNull(message = "下单业务中视频主键数组不能为空")
    @Size(min = 1, message = "下单业务中视频主键数组长度不能少于1")
    @JsonProperty("video-ids")
    private Integer[] videoIds;

    @Schema(description = "用户表主键", required = true, example = "1")
    @NotNull(message = "下单业务中用户主键不能为空")
    @JsonProperty("user-id")
    private Integer userId;

    @Schema(description = "订单总金额", required = true, example = "9999.99")
    @NotNull(message = "下单业务中订单总金额不能为空")
    @DecimalMin(value = "0", message = "下单业务中订单总金额不能小于0")
    @JsonProperty("total-fee")
    private Double totalFee;

    @Schema(description = "订单描述")
    private String info;
}

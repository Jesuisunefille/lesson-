package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Cindy
 */
@Schema(name = "CartClearParam", description = "用于清空用户购物车的Param实体参数")
@Data
public class CartClearParam implements Serializable {

    @Schema(description = "用户主键", required = true, example = "1")
    @NotNull(message = "清空购物车业务中用户主键不能为空")
    @JsonProperty("user-id")
    private Integer userId;
}

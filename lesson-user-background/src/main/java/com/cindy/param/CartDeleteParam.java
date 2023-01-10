package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @author Cindy
 */
@Schema(name = "CartDeleteParam", description = "用于删除购物车Param实体参数")
@Data
public class CartDeleteParam implements Serializable {

    @Schema(description = "用户主键", required = true, example = "1")
    @NotNull(message = "删除购物车业务中用户主键不能为空")
    @JsonProperty("user-id")
    private Integer userId;

    @Schema(description = "视频主键数组", required = true, example = "[1,2]")
    @NotNull(message = "删除购物车业务中视频主键数组不能为空")
    @JsonProperty("video-ids")
    private Integer[] videoIds;
}

package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(name = "CartInsertParam", description = "用于添加购物车Param实体参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartInsertParam implements Serializable {

    @Schema(description = "用户主键", required = true, example = "1")
    @NotNull(message = "添加购物车业务中用户主键不能为空")
    @JsonProperty("user-id")
    private Integer userId;

    @Schema(description = "视频主键", required = true, example = "1")
    @NotNull(message = "添加购物车业务中视频主键不能为空")
    @JsonProperty("video-id")
    private Integer videoId;

    @Schema(description = "视频标题", required = true, example = "1号视频")
    @NotNull(message = "添加购物车业务中视频标题不能为空")
    private String title;

    @Schema(description = "视频封面", required = true, example = "1.jpg")
    @NotNull(message = "添加购物车业务中视频封面不能为空")
    @JsonProperty("cover-image")
    private String coverImage;

    @Schema(description = "视频作者", required = true, example = "赵四")
    @NotNull(message = "添加购物车业务中视频作者不能为空")
    private String author;

    @Schema(description = "视频价格", required = true, example = "999.99")
    @NotNull(message = "添加购物车业务中视频价格不能为空")
    private Double price;


}

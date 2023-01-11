package com.cindy.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Cindy
 */
@Schema(name = "VideoSearchParam", description = "用于分页搜索视频的Param实体参数")
@Data
public class VideoSearchParam implements Serializable {

    @NotNull(message = "分页搜索视频业务中视频标题不能为空")
    @Schema(description = "视频标题", required = true, example = "框架")
    private String title;

    @Min(value = 0, message = "分页搜索视频业务中当前第几页不能小于0")
    @NotNull(message = "分页搜索视频业务中当前第几页不能为空")
    @Schema(description = "当前第几页", required = true, example = "1")
    private Integer page;

    @Min(value = 0, message = "分页搜索视频业务中每页多少条不能小于0")
    @NotNull(message = "分页搜索视频业务中每页多少条不能为空")
    @Schema(description = "每页多少条", required = true, example = "5")
    private Integer size;
}

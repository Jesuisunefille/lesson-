package com.cindy.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cindy
 */
@Schema(name = "VideoPageParam", description = "用于分页查询视频的Param实体参数")
@Data
public class VideoPageParam implements Serializable {

    @Schema(description = "当前第几页", required = true, example = "1")
    private Integer page;

    @Schema(description = "每页多少条", required = true, example = "5")
    private Integer size;
}

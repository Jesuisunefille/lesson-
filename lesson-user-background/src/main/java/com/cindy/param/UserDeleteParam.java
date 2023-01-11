package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Cindy
 */
@Schema(name = "UserDeleteParam", description = "用于注销用户的Param实体参数")
@Data
public class UserDeleteParam implements Serializable {

    @Schema(description = "用户主键", required = true, example = "1")
    @NotNull(message = "注销用户业务中用户主键不能为空")
    @JsonProperty("user-id")
    private Integer userId;
}

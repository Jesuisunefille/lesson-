package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author cindy
 */
@Schema(name = "UserUpdatePasswordParam", description = "用于用户修改密码的Param实体参数")
@Data
public class UserUpdatePasswordParam implements Serializable {

    @Schema(description = "用户表主键", required = true, example = "1")
    @NotNull(message = "修改密码业务中主键不能为空")
    private Integer id;

    @Schema(description = "用户原密码", required = true, example = "123456")
    @NotEmpty(message = "修改密码业务中原密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "原密码必须由4-16位的数字，字母，下划线或横线组成")
    @JsonProperty("old-password")
    private String oldPassword;

    @Schema(description = "用户新密码", required = true, example = "654321")
    @NotEmpty(message = "修改密码业务中新密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "新密码必须由4-16位的数字，字母，下划线或横线组成")
    @JsonProperty("new-password")
    private String newPassword;
}

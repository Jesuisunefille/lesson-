package com.cindy.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author cindy
 */
@Schema(name = "UserLoginParam", description = "用于用户登录的Param实体参数")
@Data
public class UserLoginParam implements Serializable {

        @Schema(description = "登录账号", required = true, example = "zhaosi")
        @NotEmpty(message = "登录业务中账号不能为空")
        @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "登录账号必须由3-20位的数字，字母，下划线或横线组成")
        private String username;

        @Schema(description = "登录密码", required = true, example = "zhaosi")
        @NotEmpty(message = "登录业务中密码不能为空")
        @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "登录密码必须由3-20位的数字，字母，下划线或横线组成")
        private String password;
}

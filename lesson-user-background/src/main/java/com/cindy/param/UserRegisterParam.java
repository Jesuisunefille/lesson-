package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author cindy
 */
@Schema(name = "UserRegisterParam", description = "用于用户注册的Param实体参数")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterParam implements Serializable {

    @Schema(description = "注册账号", required = true, example = "jack")
    @NotEmpty(message = "注册业务中账号不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "登录账号必须由4-16位的数字，字母，下划线或横线组成")
    private String username;

    @Schema(description = "注册密码", required = true, example = "jack")
    @NotEmpty(message = "注册业务中密码不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "登录密码必须由4-16位的数字，字母，下划线或横线组成")
    private String password;

    @JsonProperty("real-name")
    @Schema(description = "用户真实姓名", required = true, example = "杰克")
    @NotEmpty(message = "注册业务中姓名不能为空")
    @Length(min = 2, max = 15, message = "注册业务中昵称长度必须在2-15之间")
    private String realName;

    @JsonProperty("id-card")
    @Schema(description = "身份证号", required = true, example = "230107199912211020")
    @NotEmpty(message = "注册业务中身份证号不能为空")
    @Pattern(regexp = "^\\d{18}$", message = "身份证号必须由18位数字组成")
    private String idCard;

    @Schema(description = "手机号码", required = true, example = "18273636362")
    @NotEmpty(message = "注册业务中手机号不能为空")
    @Pattern(regexp = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String phone;
}

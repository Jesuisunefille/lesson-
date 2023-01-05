package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@Schema(name = "UserLoginByPhoneParam", description = "用于用户按手机号码和验证码进行登录的Param实体参数")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginByPhoneParam implements Serializable {

    @Schema(description = "验证码", required = true, example = "1234")
    @NotEmpty(message = "登录业务中验证码不能为空")
    @Pattern(regexp = "^\\w{4}$", message = "验证码必须由4位数字和字母组成")
    @JsonProperty("verification-code")
    private String verificationCode;

    @Schema(description = "手机号码", required = true, example = "15546128685")
    @NotEmpty(message = "登录业务中手机号不能为空")
    @Pattern(regexp = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String phone;
}

package com.cindy.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Cindy
 */
@Schema(name = "UserUpdateParam", description = "用于用户修改的Param实体参数")
@Data
public class UserUpdateParam implements Serializable {

    @Schema(description = "用户表主键", required = true, example = "1")
    @NotNull(message = "修改业务中主键不能为空")
    private Integer id;

    @Schema(description = "用户新昵称", required = true, example = "赵英杰")
    @Length(min = 2, max = 15, message = "修改业务中昵称长度必须在2-15之间")
    @JsonProperty("nick-name")
    private String nickName;

    @Schema(description = "用户新性别", required = true, example = "1")
    @Range(min = 0, max = 2, message = "修改业务中性别不能使用除0，1，2三个值之外的值")
    private Integer gender;

    @Schema(description = "用户新年龄", required = true, example = "35")
    @Range(min = 16, max = 60, message = "修改业务中年龄不能在16-60岁范围之外")
    private Integer age;

    @Schema(description = "用户新描述", required = true, example = "新描述")
    @Length(max = 1024, message = "修改业务中描述内容长度不能在0-1024范围之外")
    private String info;
}

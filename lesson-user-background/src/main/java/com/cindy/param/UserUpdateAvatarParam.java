package com.cindy.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author cindy
 */
@Schema(name = "UserUpdateAvatarParam", description = "用于用户修改头像Param实体参数")
@Data
public class UserUpdateAvatarParam implements Serializable {

    @Schema(description = "用户表主键", required = true, example = "1")
    @NotNull(message = "修改头像业务中主键不能为空")
    private Integer id;

    @Schema(description = "用户头像文件", required = true)
    @NotNull(message = "修改头像文件不能为空")
    private MultipartFile avatarFile;
}

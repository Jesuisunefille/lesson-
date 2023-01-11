package com.cindy.vo;

import com.cindy.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Cindy
 */
@Schema(name = "UserVo", description = "用户VO实体")
@Data
@JsonIgnoreProperties("handler")
public class UserLoginVo implements Serializable {

    private User user;

    private String token;
}

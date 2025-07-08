package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {

    @NotBlank(message = "登录账号不能为空")
    private String username; // 可以是邮箱或昵称

    @NotBlank(message = "密码不能为空")
    private String password;
}

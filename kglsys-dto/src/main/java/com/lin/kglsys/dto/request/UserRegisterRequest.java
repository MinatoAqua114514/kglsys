package com.lin.kglsys.dto.request;

import com.lin.kglsys.domain.valobj.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20位之间")
    private String password;

    @NotNull(message = "必须选择一个角色")
    private UserRole role; // 使用枚举确保角色有效性

    @NotNull(message = "验证码不能为空")
    @Size(min = 4, max = 4, message = "验证码必须是有效的四位数字")
    private String verifyCode;
}
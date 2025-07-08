package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserProfileRequest {

    @NotBlank(message = "昵称不能为空")
    @Size(max = 100, message = "昵称长度不能超过100个字符")
    private String nickname;

    @Size(max = 512, message = "头像URL过长")
    private String avatarUrl;

    @Size(max = 100, message = "学校名称过长")
    private String school;

    @Size(max = 100, message = "专业名称过长")
    private String major;

    @Size(max = 50, message = "年级信息过长")
    private String grade;
}
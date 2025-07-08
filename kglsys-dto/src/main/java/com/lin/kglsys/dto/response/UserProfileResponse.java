package com.lin.kglsys.dto.response;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String email;
    private String nickname;
    private String avatarUrl;
    private String school;
    private String major;
    private String grade;
}
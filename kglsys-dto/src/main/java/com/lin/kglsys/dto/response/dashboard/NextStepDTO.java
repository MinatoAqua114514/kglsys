package com.lin.kglsys.dto.response.dashboard;

import lombok.Data;

@Data
public class NextStepDTO {
    private String title;
    private String description;
    private String buttonText;
    private String path; // 前端路由路径
}
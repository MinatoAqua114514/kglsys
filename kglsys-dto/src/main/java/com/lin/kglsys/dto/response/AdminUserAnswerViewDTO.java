package com.lin.kglsys.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminUserAnswerViewDTO {
    private Long id; // 答卷记录ID

    // 用户信息
    private Long userId;
    private String userEmail;
    private String userNickname;

    // 问题信息
    private Integer questionId;
    private String questionText;

    // 所选答案信息
    private Integer selectedOptionId;
    private String selectedOptionText;

    // 提交时间
    private LocalDateTime createdAt;
}
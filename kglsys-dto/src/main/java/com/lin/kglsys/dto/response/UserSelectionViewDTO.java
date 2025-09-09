package com.lin.kglsys.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserSelectionViewDTO {
    // 用户信息
    private Long userId;
    private String userEmail;
    private String userNickname;

    // 岗位信息
    private Integer positionId;
    private String positionName;

    // 学习路径信息
    private Long learningPathId;
    private String learningPathName;

    // 状态更新时间
    private LocalDateTime updatedAt;
}
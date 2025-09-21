package com.lin.kglsys.dto.response;

import com.lin.kglsys.domain.valobj.UserLearningProgressStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserProgressNodeDTO {
    private Long nodeId;
    private String nodeTitle;
    private Integer sequence;
    private UserLearningProgressStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;
}
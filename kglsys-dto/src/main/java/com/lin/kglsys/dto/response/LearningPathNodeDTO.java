package com.lin.kglsys.dto.response;

import com.lin.kglsys.domain.valobj.UserLearningProgressStatus;
import lombok.Data;

@Data
public class LearningPathNodeDTO {
    private Long id;
    private String nodeTitle;
    private String nodeDescription;
    private Integer sequence;
    private Integer estimatedHours;
    private UserLearningProgressStatus status;
}
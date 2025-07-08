package com.lin.kglsys.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class LearningPathDTO {
    private Long id;
    private String name;
    private String description;
    private String difficultyLevel;
    private List<LearningPathNodeDTO> nodes;
}
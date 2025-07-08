package com.lin.kglsys.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class KgEntityDetailDTO {
    private Long id;
    private String name;
    private String type;
    private String description;
    private List<LearningResourceDTO> learningResources;
    private List<ProblemBriefDTO> problems;
}
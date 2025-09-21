package com.lin.kglsys.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class LinkedContentDTO {
    private List<LearningResourceDTO> learningResources;
    private List<ProblemBriefDTO> problems;
}
package com.lin.kglsys.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ProblemDetailDTO {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private Integer timeLimitMs;
    private Integer memoryLimitKb;
    private List<String> tags;
    private List<TestCaseDTO> sampleTestCases;
}
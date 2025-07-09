package com.lin.kglsys.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeTaskDTO {
    private String submissionId;
    private String sourceCode;
    private int languageId;
    private List<TestCaseDTO> testCases;
    private float timeLimitS;
    private int memoryLimitKb;
}
package com.lin.kglsys.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeTaskDTO {

    // 使用 @JsonProperty 指定序列化后的 JSON 键名
    @JsonProperty("submission_id")
    private String submissionId;

    @JsonProperty("source_code")
    private String sourceCode;

    @JsonProperty("language_id")
    private int languageId;

    @JsonProperty("test_cases")
    private List<TestCaseDTO> testCases;

    @JsonProperty("time_limit_s")
    private float timeLimitS;

    @JsonProperty("memory_limit_kb")
    private int memoryLimitKb;
}
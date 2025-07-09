package com.lin.kglsys.dto.mq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JudgeResultDTO {
    @JsonProperty("submission_id")
    private String submissionId;

    private String status;

    @JsonProperty("execution_time_ms")
    private Integer executionTimeMs;

    @JsonProperty("memory_used_kb")
    private Integer memoryUsedKb;

    @JsonProperty("judge_output")
    private String judgeOutput;
}
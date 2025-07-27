package com.lin.kglsys.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestCaseDTO {
    // 使用 @JsonProperty 指定序列化后的 JSON 键名
    @JsonProperty("input")
    private String input;

    @JsonProperty("expected_output")
    private String expectedOutput;
}
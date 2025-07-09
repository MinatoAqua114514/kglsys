package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CodeSubmissionRequest {

    @NotNull(message = "题目ID不能为空")
    private Long problemId;

    @NotBlank(message = "源代码不能为空")
    private String sourceCode;

    @NotNull(message = "语言ID不能为空")
    private Integer languageId; // 与Judge0的语言ID对应
}
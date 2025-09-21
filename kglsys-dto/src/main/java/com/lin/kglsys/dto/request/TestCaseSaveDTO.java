package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestCaseSaveDTO {
    // 更新时需要ID，创建时可为空
    private Long id;

    @NotBlank(message = "输入不能为空")
    private String input;

    @NotBlank(message = "预期输出不能为空")
    private String expectedOutput;

    @NotNull(message = "必须指定是否为示例")
    private Boolean isSample;

    @NotNull(message = "顺序不能为空")
    private Integer sequence;
}
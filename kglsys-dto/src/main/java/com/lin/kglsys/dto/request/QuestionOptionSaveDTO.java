package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionOptionSaveDTO {
    // 更新时需要ID，创建时可为空
    private Integer id;

    @NotBlank(message = "选项内容不能为空")
    private String optionText;

    @NotNull(message = "选项顺序不能为空")
    private Integer sequence;
}
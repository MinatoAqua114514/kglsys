package com.lin.kglsys.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssessmentQuestionSaveDTO {
    // 更新时需要ID，创建时可为空
    private Integer id;

    @NotBlank(message = "题干内容不能为空")
    private String questionText;

    @NotNull(message = "问题顺序不能为空")
    private Integer sequence;

    @NotNull(message = "激活状态不能为空")
    private Boolean isActive;

    @NotEmpty(message = "问题至少需要一个选项")
    @Valid // 级联校验列表中的每个选项DTO
    private List<QuestionOptionSaveDTO> options;
}
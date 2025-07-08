package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerDTO {
    @NotNull(message = "问题ID不能为空")
    private Integer questionId;

    @NotNull(message = "选项ID不能为空")
    private Integer selectedOptionId;
}
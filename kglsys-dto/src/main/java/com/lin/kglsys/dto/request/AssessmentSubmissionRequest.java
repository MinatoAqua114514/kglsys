package com.lin.kglsys.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class AssessmentSubmissionRequest {
    @NotEmpty(message = "答案列表不能为空")
    @Valid // 级联校验列表中的每个AnswerDTO
    private List<AnswerDTO> answers;
}
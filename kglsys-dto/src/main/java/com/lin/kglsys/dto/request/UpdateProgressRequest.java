package com.lin.kglsys.dto.request;

import com.lin.kglsys.domain.valobj.UserLearningProgressStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateProgressRequest {
    @NotNull(message = "学习状态不能为空")
    private UserLearningProgressStatus newStatus;
}
package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SelectTargetPositionRequest {
    @NotNull(message = "岗位ID不能为空")
    private Integer positionId;
}
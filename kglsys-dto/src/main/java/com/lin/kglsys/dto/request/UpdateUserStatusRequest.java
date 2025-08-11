package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    @NotNull(message = "用户状态不能为空")
    private Boolean enabled;
}
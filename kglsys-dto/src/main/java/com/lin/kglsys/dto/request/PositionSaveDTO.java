package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PositionSaveDTO {

    @NotBlank(message = "岗位内部名称不能为空")
    @Size(max = 100, message = "岗位内部名称长度不能超过100个字符")
    private String name;

    @NotBlank(message = "岗位显示名称不能为空")
    @Size(max = 100, message = "岗位显示名称长度不能超过100个字符")
    private String displayName;

    @NotBlank(message = "岗位描述不能为空")
    private String description;

    private String responsibilities;

    @NotBlank(message = "技能要求不能为空")
    private String skillRequirements;
}
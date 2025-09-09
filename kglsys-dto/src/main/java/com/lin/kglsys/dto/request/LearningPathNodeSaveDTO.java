package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LearningPathNodeSaveDTO {
    // 更新时需要ID，创建时可为空
    private Long id;

    @NotBlank(message = "节点标题不能为空")
    private String nodeTitle;

    private String nodeDescription;

    @NotNull(message = "节点顺序不能为空")
    private Integer sequence;

    private Integer estimatedHours;
}
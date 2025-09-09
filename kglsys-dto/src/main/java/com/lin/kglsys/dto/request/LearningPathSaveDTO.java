package com.lin.kglsys.dto.request;

import com.lin.kglsys.domain.valobj.DifficultyLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class LearningPathSaveDTO {

    @NotBlank(message = "学习路径名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "难度等级不能为空")
    private DifficultyLevel difficultyLevel;

    @NotNull(message = "是否为默认路径不能为空")
    private Boolean isDefault;

    @Valid // 级联校验列表中的每个节点DTO
    private List<LearningPathNodeSaveDTO> nodes;
}
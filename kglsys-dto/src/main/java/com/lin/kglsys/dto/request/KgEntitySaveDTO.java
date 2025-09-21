package com.lin.kglsys.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class KgEntitySaveDTO {

    @NotBlank(message = "实体唯一标识符不能为空")
    @Size(max = 100, message = "实体唯一标识符长度不能超过100")
    private String entityIdStr;

    @NotBlank(message = "实体名称不能为空")
    private String name;

    @NotBlank(message = "实体类型不能为空")
    @Size(max = 100, message = "实体类型长度不能超过100")
    private String type;

    private String description;
}
package com.lin.kglsys.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class EntityLinkRequestDTO {
    // 关联的学习资源ID列表
    private List<Long> resourceIds;

    // 关联的编程题目ID列表
    private List<Long> problemIds;
}
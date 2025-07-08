package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.response.KgEntityDetailDTO;

public interface KnowledgeGraphService {

    /**
     * 获取知识实体的详细信息，包括关联的学习资源和编程题目
     * @param entityId 知识实体ID
     * @return 实体详情DTO
     */
    KgEntityDetailDTO getEntityDetails(Long entityId);
}
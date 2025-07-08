package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.response.LearningPathDTO;
import com.lin.kglsys.dto.response.graph.KnowledgeGraphDTO;

public interface LearningPathService {

    /**
     * 根据岗位ID获取其默认学习路径
     * @param positionId 岗位ID
     * @return 学习路径DTO
     */
    LearningPathDTO getLearningPathForPosition(Integer positionId);

    /**
     * 根据学习路径节点ID获取其内部的知识图谱
     * @param nodeId 学习路径节点ID
     * @return 知识图谱DTO
     */
    KnowledgeGraphDTO getNodeKnowledgeGraph(Long nodeId);
}
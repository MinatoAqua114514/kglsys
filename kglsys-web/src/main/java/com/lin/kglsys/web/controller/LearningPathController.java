package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.LearningPathService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.response.LearningPathDTO;
import com.lin.kglsys.dto.response.graph.KnowledgeGraphDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LearningPathController {

    private final LearningPathService learningPathService;

    /**
     * UR-008: 获取指定岗位的默认学习路径
     * @param positionId 岗位ID
     * @return 学习路径详情
     */
    @GetMapping("/positions/{positionId}/learning-path")
    public ApiResult<LearningPathDTO> getLearningPathForPosition(@PathVariable Integer positionId) {
        LearningPathDTO learningPath = learningPathService.getLearningPathForPosition(positionId);
        return ApiResult.success(learningPath);
    }

    /**
     * UR-009: 获取学习路径节点的知识图谱
     * @param nodeId 学习路径节点ID
     * @return 知识图谱数据
     */
    @GetMapping("/learning-path-nodes/{nodeId}/graph")
    public ApiResult<KnowledgeGraphDTO> getNodeKnowledgeGraph(@PathVariable Long nodeId) {
        KnowledgeGraphDTO graph = learningPathService.getNodeKnowledgeGraph(nodeId);
        return ApiResult.success(graph);
    }
}
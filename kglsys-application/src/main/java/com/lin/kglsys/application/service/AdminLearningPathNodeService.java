package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.LearningPathNodeSaveDTO;
import com.lin.kglsys.dto.response.LearningPathNodeDTO;

import java.util.List;

public interface AdminLearningPathNodeService {

    /**
     * 获取指定学习路径下的所有节点
     * @param pathId 学习路径ID
     * @return 节点DTO列表
     */
    List<LearningPathNodeDTO> listNodesForPath(Long pathId);

    /**
     * 在指定学习路径下创建新节点
     * @param pathId 学习路径ID
     * @param dto 节点保存DTO
     * @return 创建后的节点DTO
     */
    LearningPathNodeDTO createNode(Long pathId, LearningPathNodeSaveDTO dto);

    /**
     * 更新指定ID的节点
     * @param nodeId 节点ID
     * @param dto 节点保存DTO
     * @return 更新后的节点DTO
     */
    LearningPathNodeDTO updateNode(Long nodeId, LearningPathNodeSaveDTO dto);

    /**
     * 删除指定ID的节点
     * @param nodeId 节点ID
     */
    void deleteNode(Long nodeId);
}
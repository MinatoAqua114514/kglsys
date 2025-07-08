package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.LearningPathMapper;
import com.lin.kglsys.application.service.LearningPathService;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.domain.entity.*;
import com.lin.kglsys.domain.valobj.UserLearningProgressStatus;
import com.lin.kglsys.dto.response.LearningPathDTO;
import com.lin.kglsys.dto.response.graph.KnowledgeGraphDTO;
import com.lin.kglsys.infra.repository.KgRelationshipRepository;
import com.lin.kglsys.infra.repository.LearningPathNodeRepository;
import com.lin.kglsys.infra.repository.LearningPathRepository;
import com.lin.kglsys.infra.repository.PathNodeEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.dto.response.LearningPathNodeDTO;
import com.lin.kglsys.infra.repository.UserLearningProgressRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LearningPathServiceImpl implements LearningPathService {

    private final LearningPathRepository learningPathRepository;
    private final LearningPathNodeRepository learningPathNodeRepository;
    private final PathNodeEntityRepository pathNodeEntityRepository;
    private final KgRelationshipRepository kgRelationshipRepository;
    private final UserLearningProgressRepository progressRepository;
    private final LearningPathMapper learningPathMapper;

    @Override
    @Transactional(readOnly = true)
    public LearningPathDTO getLearningPathForPosition(Integer positionId) {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) throw new UserNotFoundException();

        LearningPath path = learningPathRepository.findByPositionIdAndIsDefault(positionId, true)
                .orElseThrow(() -> new ResourceNotFoundException("该岗位暂无默认学习路径"));

        List<LearningPathNode> nodes = learningPathNodeRepository.findByPathIdOrderBySequenceAsc(path.getId());

        // [新增] 获取用户在该路径上的所有进度记录，并转为Map以便快速查找
        Map<Long, UserLearningProgressStatus> progressMap = progressRepository.findUserProgressForPath(userId, path.getId())
                .stream()
                .collect(Collectors.toMap(p -> p.getPathNode().getId(), UserLearningProgress::getStatus));

        // [修改] 组装DTO，并为每个节点设置学习状态
        LearningPathDTO pathDTO = learningPathMapper.toLearningPathDTO(path);
        List<LearningPathNodeDTO> nodeDTOs = nodes.stream().map(node -> {
            LearningPathNodeDTO nodeDTO = learningPathMapper.toLearningPathNodeDTO(node);
            // 从Map中获取状态，如果不存在则默认为 NOT_STARTED
            nodeDTO.setStatus(progressMap.getOrDefault(node.getId(), UserLearningProgressStatus.NOT_STARTED));
            return nodeDTO;
        }).collect(Collectors.toList());
        pathDTO.setNodes(nodeDTOs);

        return pathDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public KnowledgeGraphDTO getNodeKnowledgeGraph(Long nodeId) {
        // 1. 查找该路径节点关联的所有知识实体
        List<PathNodeEntity> pathNodeEntities = pathNodeEntityRepository.findByPathNodeIdWithEntities(nodeId);
        if (pathNodeEntities.isEmpty()) {
            KnowledgeGraphDTO emptyGraph = new KnowledgeGraphDTO();
            emptyGraph.setNodes(Collections.emptyList());
            emptyGraph.setLinks(Collections.emptyList());
            return emptyGraph;
        }

        List<KgEntity> entities = pathNodeEntities.stream()
                .map(PathNodeEntity::getEntity)
                .collect(Collectors.toList());
        List<Long> entityIds = entities.stream().map(KgEntity::getId).collect(Collectors.toList());

        // 2. 查找这些实体之间的所有关系
        List<KgRelationship> relationships = kgRelationshipRepository.findBySourceEntityIdInAndTargetEntityIdIn(entityIds, entityIds);

        // 3. 组装成图DTO
        KnowledgeGraphDTO graphDTO = new KnowledgeGraphDTO();
        graphDTO.setNodes(learningPathMapper.toKgNodeDTOList(entities));
        graphDTO.setLinks(learningPathMapper.toKgLinkDTOList(relationships));

        return graphDTO;
    }
}
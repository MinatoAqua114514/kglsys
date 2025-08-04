package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.LearningPathMapper;
import com.lin.kglsys.application.service.LearningPathService;
import com.lin.kglsys.common.exception.business.PermissionDeniedException;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.domain.entity.*;
import com.lin.kglsys.domain.valobj.UserLearningProgressStatus;
import com.lin.kglsys.dto.response.LearningPathDTO;
import com.lin.kglsys.dto.response.graph.KnowledgeGraphDTO;
import com.lin.kglsys.infra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.dto.response.LearningPathNodeDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
    private final UserLearningStatusRepository userLearningStatusRepository;

    @Override
    @Transactional(readOnly = true)
    public LearningPathDTO getLearningPathForPosition(Integer positionId) {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) throw new UserNotFoundException();

        // --- 新验证授权 ---
        // 1. 获取用户的学习状态以找到您的真实目标职位。
        UserLearningStatus learningStatus = userLearningStatusRepository.findByIdWithDetails(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Status de aprendizagem do usuário não encontrado."));

        // 2. 验证用户是否有一个活跃的目标职位。
        if (learningStatus.getActivePath() == null || learningStatus.getActivePath().getPosition() == null) {
            throw new PermissionDeniedException(); // Ou uma exceção de negócio mais específica.
        }

        // 3. 比较请求的职位ID与用户实际目标职位ID
        Integer actualTargetPositionId = learningStatus.getActivePath().getPosition().getId();
        if (!Objects.equals(positionId, actualTargetPositionId)) {
            // 如果不匹配，拒绝访问。
            throw new PermissionDeniedException();
        }
        // --- 授权验证结束 ---

        // 后续逻辑只有在授权验证通过的情况下才会执行。
        LearningPath path = learningPathRepository.findByPositionIdAndIsDefault(positionId, true)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma trilha de aprendizagem padrão encontrada para este cargo"));

        List<LearningPathNode> nodes = learningPathNodeRepository.findByPathIdOrderBySequenceAsc(path.getId());

        Map<Long, UserLearningProgressStatus> progressMap = progressRepository.findUserProgressForPath(userId, path.getId())
                .stream()
                .collect(Collectors.toMap(p -> p.getPathNode().getId(), UserLearningProgress::getStatus));

        LearningPathDTO pathDTO = learningPathMapper.toLearningPathDTO(path);
        List<LearningPathNodeDTO> nodeDTOs = nodes.stream().map(node -> {
            LearningPathNodeDTO nodeDTO = learningPathMapper.toLearningPathNodeDTO(node);
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
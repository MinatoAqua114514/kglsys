package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.KnowledgeGraphMapper;
import com.lin.kglsys.application.service.KnowledgeGraphService;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.domain.entity.EntityLearningResource;
import com.lin.kglsys.domain.entity.KgEntity;
import com.lin.kglsys.domain.entity.ProblemEntity;
import com.lin.kglsys.dto.response.KgEntityDetailDTO;
import com.lin.kglsys.dto.response.LearningResourceDTO;
import com.lin.kglsys.dto.response.ProblemBriefDTO;
import com.lin.kglsys.infra.repository.EntityLearningResourceRepository;
import com.lin.kglsys.infra.repository.KgEntityRepository;
import com.lin.kglsys.infra.repository.ProblemEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KnowledgeGraphServiceImpl implements KnowledgeGraphService {

    private final KgEntityRepository kgEntityRepository;
    private final EntityLearningResourceRepository resourceRepository;
    private final ProblemEntityRepository problemRepository;
    private final KnowledgeGraphMapper knowledgeGraphMapper;

    @Override
    @Transactional(readOnly = true)
    public KgEntityDetailDTO getEntityDetails(Long entityId) {
        // 1. 查找实体本身
        KgEntity entity = kgEntityRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("知识实体不存在"));

        // 2. 查找关联的学习资源
        List<EntityLearningResource> relatedResources = resourceRepository.findByEntityIdWithResources(entityId);
        List<LearningResourceDTO> resourceDTOs = relatedResources.stream()
                .map(elr -> knowledgeGraphMapper.toLearningResourceDTO(elr.getResource()))
                .collect(Collectors.toList());

        // 3. 查找关联的编程题目
        List<ProblemEntity> relatedProblems = problemRepository.findByEntityIdWithProblems(entityId);
        List<ProblemBriefDTO> problemDTOs = relatedProblems.stream()
                .map(pe -> knowledgeGraphMapper.toProblemBriefDTO(pe.getProblem()))
                .collect(Collectors.toList());

        // 4. 聚合到 DTO
        KgEntityDetailDTO detailDTO = knowledgeGraphMapper.toKgEntityDetailDTO(entity);
        detailDTO.setLearningResources(resourceDTOs);
        detailDTO.setProblems(problemDTOs);

        return detailDTO;
    }
}
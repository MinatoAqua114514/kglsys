package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.KnowledgeGraphMapper;
import com.lin.kglsys.application.service.AdminEntityContentService;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.domain.entity.*;
import com.lin.kglsys.domain.valobj.TestDepth;
import com.lin.kglsys.dto.request.EntityLinkRequestDTO;
import com.lin.kglsys.dto.response.LinkedContentDTO;
import com.lin.kglsys.infra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEntityContentServiceImpl implements AdminEntityContentService {

    private final KgEntityRepository entityRepository;
    private final LearningResourceRepository learningResourceRepository;
    private final ProblemRepository problemRepository;
    private final EntityLearningResourceRepository entityResourceRepository;
    private final ProblemEntityRepository problemEntityRepository;
    private final KnowledgeGraphMapper kgMapper;

    @Override
    @Transactional(readOnly = true)
    public LinkedContentDTO getLinkedContent(Long entityId) {
        if (!entityRepository.existsById(entityId)) {
            throw new ResourceNotFoundException("知识实体不存在");
        }

        LinkedContentDTO contentDTO = new LinkedContentDTO();

        // 获取关联的学习资源
        List<EntityLearningResource> resources = entityResourceRepository.findByEntityIdWithResources(entityId);
        contentDTO.setLearningResources(resources.stream()
                .map(er -> kgMapper.toLearningResourceDTO(er.getResource()))
                .collect(Collectors.toList()));

        // 获取关联的编程题目
        List<ProblemEntity> problems = problemEntityRepository.findByEntityIdWithProblems(entityId);
        contentDTO.setProblems(problems.stream()
                .map(pe -> kgMapper.toProblemBriefDTO(pe.getProblem()))
                .collect(Collectors.toList()));

        return contentDTO;
    }

    @Override
    @Transactional
    public LinkedContentDTO updateLinkedContent(Long entityId, EntityLinkRequestDTO dto) {
        KgEntity entity = entityRepository.findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException("知识实体不存在"));

        // --- 更新学习资源关联 ---
        entityResourceRepository.deleteByEntityId(entityId); // 采用先删后增的简单策略
        if (dto.getResourceIds() != null && !dto.getResourceIds().isEmpty()) {
            List<EntityLearningResource> newLinks = new ArrayList<>();
            for (Long resourceId : dto.getResourceIds()) {
                LearningResource resource = learningResourceRepository.findById(resourceId)
                        .orElseThrow(() -> new ResourceNotFoundException("学习资源不存在: " + resourceId));
                EntityLearningResource link = new EntityLearningResource();
                link.setEntity(entity);
                link.setResource(resource);
                newLinks.add(link);
            }
            entityResourceRepository.saveAll(newLinks);
        }

        // --- 更新编程题目关联 ---
        problemEntityRepository.deleteByEntityId(entityId); // 同样采用先删后增
        if (dto.getProblemIds() != null && !dto.getProblemIds().isEmpty()) {
            List<ProblemEntity> newLinks = new ArrayList<>();
            for (Long problemId : dto.getProblemIds()) {
                Problem problem = problemRepository.findById(problemId)
                        .orElseThrow(() -> new ResourceNotFoundException("编程题目不存在: " + problemId));
                ProblemEntity link = new ProblemEntity();
                link.setEntity(entity);
                link.setProblem(problem);
                link.setTestDepth(TestDepth.BASIC); // 默认为基础，可后续扩展
                newLinks.add(link);
            }
            problemEntityRepository.saveAll(newLinks);
        }

        return getLinkedContent(entityId);
    }
}
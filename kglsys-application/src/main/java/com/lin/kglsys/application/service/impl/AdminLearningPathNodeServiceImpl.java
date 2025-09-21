package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.LearningPathMapper;
import com.lin.kglsys.application.service.AdminLearningPathNodeService;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.domain.entity.LearningPath;
import com.lin.kglsys.domain.entity.LearningPathNode;
import com.lin.kglsys.dto.request.LearningPathNodeSaveDTO;
import com.lin.kglsys.dto.response.LearningPathNodeDTO;
import com.lin.kglsys.infra.repository.LearningPathNodeRepository;
import com.lin.kglsys.infra.repository.LearningPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminLearningPathNodeServiceImpl implements AdminLearningPathNodeService {

    private final LearningPathNodeRepository nodeRepository;
    private final LearningPathRepository pathRepository;
    private final LearningPathMapper pathMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LearningPathNodeDTO> listNodesForPath(Long pathId) {
        if (!pathRepository.existsById(pathId)) {
            throw new ResourceNotFoundException("学习路径不存在");
        }
        List<LearningPathNode> nodes = nodeRepository.findByPathIdOrderBySequenceAsc(pathId);
        return pathMapper.toLearningPathNodeDTOList(nodes);
    }

    @Override
    @Transactional
    public LearningPathNodeDTO createNode(Long pathId, LearningPathNodeSaveDTO dto) {
        LearningPath path = pathRepository.findById(pathId)
                .orElseThrow(() -> new ResourceNotFoundException("关联的学习路径不存在"));

        LearningPathNode node = pathMapper.nodeFromSaveDTO(dto);
        node.setPath(path);
        node.setCreatedAt(LocalDateTime.now());

        LearningPathNode savedNode = nodeRepository.save(node);
        return pathMapper.toLearningPathNodeDTO(savedNode);
    }

    @Override
    @Transactional
    public LearningPathNodeDTO updateNode(Long nodeId, LearningPathNodeSaveDTO dto) {
        LearningPathNode node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new ResourceNotFoundException("要更新的节点不存在"));

        pathMapper.updateNodeFromSaveDTO(dto, node);
        LearningPathNode updatedNode = nodeRepository.save(node);
        return pathMapper.toLearningPathNodeDTO(updatedNode);
    }

    @Override
    @Transactional
    public void deleteNode(Long nodeId) {
        if (!nodeRepository.existsById(nodeId)) {
            throw new ResourceNotFoundException("要删除的节点不存在");
        }
        // TODO 可以在此添加业务检查，例如：如果节点被其他节点依赖，则不允许删除
        nodeRepository.deleteById(nodeId);
    }
}
package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.LearningPathMapper;
import com.lin.kglsys.application.service.AdminLearningPathService;
import com.lin.kglsys.common.exception.business.InvalidParameterException;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.domain.entity.LearningPath;
import com.lin.kglsys.domain.entity.LearningPathNode;
import com.lin.kglsys.domain.entity.Position;
import com.lin.kglsys.dto.request.LearningPathNodeSaveDTO;
import com.lin.kglsys.dto.request.LearningPathSaveDTO;
import com.lin.kglsys.dto.response.LearningPathDTO;
import com.lin.kglsys.infra.repository.LearningPathNodeRepository;
import com.lin.kglsys.infra.repository.LearningPathRepository;
import com.lin.kglsys.infra.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminLearningPathServiceImpl implements AdminLearningPathService {

    private final LearningPathRepository pathRepository;
    private final LearningPathNodeRepository nodeRepository;
    private final PositionRepository positionRepository;
    private final LearningPathMapper pathMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LearningPathDTO> listPathsForPosition(Integer positionId) {
        if (!positionRepository.existsById(positionId)) {
            throw new ResourceNotFoundException("关联的岗位不存在");
        }
        List<LearningPath> paths = pathRepository.findByPositionId(positionId);
        return pathMapper.toLearningPathDTOList(paths);
    }

    @Override
    @Transactional(readOnly = true)
    public LearningPathDTO getPathById(Long pathId) {
        LearningPath path = pathRepository.findById(pathId)
                .orElseThrow(() -> new ResourceNotFoundException("学习路径不存在"));
        List<LearningPathNode> nodes = nodeRepository.findByPathIdOrderBySequenceAsc(pathId);

        LearningPathDTO dto = pathMapper.toLearningPathDTO(path);
        dto.setNodes(pathMapper.toLearningPathNodeDTOList(nodes));
        return dto;
    }

    @Override
    @Transactional
    public LearningPathDTO createPath(Integer positionId, LearningPathSaveDTO dto) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("关联的岗位不存在"));

        // 唯一性校验
        pathRepository.findByPositionIdAndName(positionId, dto.getName()).ifPresent(p -> {
            throw new InvalidParameterException("该岗位下已存在同名学习路径");
        });

        // 如果设为默认，取消其他路径的默认状态
        if (dto.getIsDefault()) {
            unsetOtherDefaults(positionId, null);
        }

        LearningPath path = pathMapper.fromSaveDTO(dto);
        path.setPosition(position);
        path.setCreatedAt(LocalDateTime.now());
        path.setUpdatedAt(LocalDateTime.now());
        LearningPath savedPath = pathRepository.save(path);

        if (dto.getNodes() != null && !dto.getNodes().isEmpty()) {
            List<LearningPathNode> nodes = dto.getNodes().stream().map(nodeDto -> {
                LearningPathNode node = pathMapper.nodeFromSaveDTO(nodeDto);
                node.setPath(savedPath);
                node.setCreatedAt(LocalDateTime.now());
                return node;
            }).collect(Collectors.toList());
            nodeRepository.saveAll(nodes);
        }

        return getPathById(savedPath.getId());
    }

    @Override
    @Transactional
    public LearningPathDTO updatePath(Long pathId, LearningPathSaveDTO dto) {
        LearningPath path = pathRepository.findById(pathId)
                .orElseThrow(() -> new ResourceNotFoundException("要更新的学习路径不存在"));

        // 唯一性校验
        pathRepository.findByPositionIdAndName(path.getPosition().getId(), dto.getName()).ifPresent(p -> {
            if (!Objects.equals(p.getId(), pathId)) {
                throw new InvalidParameterException("该岗位下已存在同名学习路径");
            }
        });

        // 如果设为默认，取消其他路径的默认状态
        if (dto.getIsDefault()) {
            unsetOtherDefaults(path.getPosition().getId(), pathId);
        }

        pathMapper.updateFromSaveDTO(dto, path);
        path.setUpdatedAt(LocalDateTime.now());

        updateNodesForPath(path, dto.getNodes());

        LearningPath updatedPath = pathRepository.save(path);
        return getPathById(updatedPath.getId());
    }

    @Override
    @Transactional
    public void deletePath(Long pathId) {
        if (!pathRepository.existsById(pathId)) {
            throw new ResourceNotFoundException("要删除的学习路径不存在");
        }
        // 检查依赖：是否被用户设为激活路径
        if (pathRepository.isUsedAsActivePath(pathId)) {
            throw new InvalidParameterException("无法删除：该学习路径正被用户使用");
        }
        // 删除路径将级联删除其下的所有节点
        pathRepository.deleteById(pathId);
    }

    private void unsetOtherDefaults(Integer positionId, Long currentPathId) {
        List<LearningPath> paths = pathRepository.findByPositionId(positionId);
        paths.forEach(p -> {
            if (p.isDefault() && !Objects.equals(p.getId(), currentPathId)) {
                p.setDefault(false);
            }
        });
        pathRepository.saveAll(paths);
    }

    private void updateNodesForPath(LearningPath path, List<LearningPathNodeSaveDTO> nodeDTOs) {
        List<LearningPathNode> existingNodes = nodeRepository.findByPathIdOrderBySequenceAsc(path.getId());
        Map<Long, LearningPathNode> existingNodesMap = existingNodes.stream()
                .collect(Collectors.toMap(LearningPathNode::getId, Function.identity()));

        Set<Long> dtoIds = nodeDTOs.stream()
                .map(LearningPathNodeSaveDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 删除
        List<LearningPathNode> toDelete = existingNodes.stream()
                .filter(node -> !dtoIds.contains(node.getId()))
                .collect(Collectors.toList());
        nodeRepository.deleteAll(toDelete);

        // 更新和新增
        List<LearningPathNode> toSave = new ArrayList<>();
        for (LearningPathNodeSaveDTO dto : nodeDTOs) {
            if (dto.getId() != null && existingNodesMap.containsKey(dto.getId())) {
                // 更新
                LearningPathNode nodeToUpdate = existingNodesMap.get(dto.getId());
                pathMapper.updateNodeFromSaveDTO(dto, nodeToUpdate);
                toSave.add(nodeToUpdate);
            } else {
                // 新增
                LearningPathNode newNode = pathMapper.nodeFromSaveDTO(dto);
                newNode.setPath(path);
                newNode.setCreatedAt(LocalDateTime.now());
                toSave.add(newNode);
            }
        }
        nodeRepository.saveAll(toSave);
    }
}
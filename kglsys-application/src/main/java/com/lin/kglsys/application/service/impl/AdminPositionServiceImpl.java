package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.PositionMapper;
import com.lin.kglsys.application.service.AdminPositionService;
import com.lin.kglsys.common.exception.business.InvalidParameterException;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.domain.entity.Position;
import com.lin.kglsys.dto.request.PositionSaveDTO;
import com.lin.kglsys.dto.response.PositionDTO;
import com.lin.kglsys.infra.repository.LearningPathRepository;
import com.lin.kglsys.infra.repository.PositionRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminPositionServiceImpl implements AdminPositionService {

    private final PositionRepository positionRepository;
    private final LearningPathRepository learningPathRepository;
    private final PositionMapper positionMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PositionDTO> listPositions(Pageable pageable, String keyword) {
        Specification<Position> spec = (root, query, cb) -> {
            if (!StringUtils.hasText(keyword)) {
                return cb.conjunction();
            }
            List<Predicate> predicates = new ArrayList<>();
            String pattern = "%" + keyword.toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get("name")), pattern));
            predicates.add(cb.like(cb.lower(root.get("displayName")), pattern));
            return cb.or(predicates.toArray(new Predicate[0]));
        };
        Page<Position> positionPage = positionRepository.findAll(spec, pageable);
        return positionPage.map(positionMapper::toPositionDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionDTO getPositionById(Integer id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("岗位不存在"));
        return positionMapper.toPositionDTO(position);
    }

    @Override
    @Transactional
    public PositionDTO createPosition(PositionSaveDTO dto) {
        // 1. 唯一性校验
        checkUniqueness(dto.getName(), dto.getDisplayName(), null);

        // 2. DTO转实体
        Position position = positionMapper.dtoToEntity(dto);
        LocalDateTime now = LocalDateTime.now();
        position.setCreatedAt(now);
        position.setUpdatedAt(now);

        // 3. 保存并返回
        Position savedPosition = positionRepository.save(position);
        return positionMapper.toPositionDTO(savedPosition);
    }

    @Override
    @Transactional
    public PositionDTO updatePosition(Integer id, PositionSaveDTO dto) {
        // 1. 查找现有实体
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("要更新的岗位不存在"));

        // 2. 唯一性校验 (排除自身)
        checkUniqueness(dto.getName(), dto.getDisplayName(), id);

        // 3. 更新属性
        positionMapper.updateEntityFromDto(dto, position);
        position.setUpdatedAt(LocalDateTime.now());

        // 4. 保存并返回
        Position updatedPosition = positionRepository.save(position);
        return positionMapper.toPositionDTO(updatedPosition);
    }

    @Override
    @Transactional
    public void deletePosition(Integer id) {
        // 1. 检查岗位是否存在
        if (!positionRepository.existsById(id)) {
            throw new ResourceNotFoundException("要删除的岗位不存在");
        }

        // 2. 检查依赖：是否存在关联的学习路径
        if (learningPathRepository.findByPositionIdAndIsDefault(id, true).isPresent()) {
            throw new InvalidParameterException("无法删除：该岗位已关联了学习路径，请先解除关联");
        }
        // 也可以检查其他依赖，如 OptionPositionTendency

        // 3. 执行删除
        positionRepository.deleteById(id);
    }

    /**
     * 辅助方法：检查岗位名称的唯一性
     * @param name 内部名称
     * @param displayName 显示名称
     * @param currentId 当前正在更新的岗位ID，如果是创建则为null
     */
    private void checkUniqueness(String name, String displayName, Integer currentId) {
        positionRepository.findByNameOrDisplayName(name, displayName).ifPresent(existingPosition -> {
            // 如果找到了一个同名岗位，并且这个岗位不是我们当前正在修改的岗位，则抛出异常
            if (currentId == null || !Objects.equals(existingPosition.getId(), currentId)) {
                throw new InvalidParameterException("岗位名称或显示名称已存在");
            }
        });
    }
}
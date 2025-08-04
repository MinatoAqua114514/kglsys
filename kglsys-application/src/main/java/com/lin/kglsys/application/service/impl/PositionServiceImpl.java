package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.PositionMapper;
import com.lin.kglsys.application.service.PositionService;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.domain.entity.*;
import com.lin.kglsys.domain.valobj.UserTargetSource;
import com.lin.kglsys.dto.response.PositionDTO;
import com.lin.kglsys.dto.response.RecommendedPositionDTO;
import com.lin.kglsys.infra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final UserTargetPositionRepository userTargetPositionRepository;
    private final UserRepository userRepository;
    private final PositionMapper positionMapper;
    private final UserLearningStatusRepository userLearningStatusRepository;
    private final LearningPathRepository learningPathRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RecommendedPositionDTO> getRecommendedPositions() {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) throw new UserNotFoundException();

        List<UserTargetPosition> targets = userTargetPositionRepository.findByUserId(userId);
        return targets.stream().map(target -> {
            RecommendedPositionDTO dto = positionMapper.toRecommendedPositionDTO(target.getPosition());
            dto.setMatchScore(target.getMatchScore());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PositionDTO getPositionDetails(Integer id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("岗位不存在"));
        return positionMapper.toPositionDTO(position);
    }

    @Override
    @Transactional
    public void selectTargetPosition(Integer positionId) {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) throw new UserNotFoundException();

        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("要选择的职位不存在"));

        List<UserTargetPosition> existingTargets = userTargetPositionRepository.findByUserId(userId);
        if (!existingTargets.isEmpty()) {
            userTargetPositionRepository.deleteAllInBatch(existingTargets);
        }

        UserTargetPosition newTarget = new UserTargetPosition();
        newTarget.setUser(userRepository.getReferenceById(userId));
        newTarget.setPosition(position);
        newTarget.setSource(UserTargetSource.MANUAL);
        newTarget.setCreatedAt(LocalDateTime.now());
        userTargetPositionRepository.save(newTarget);

        LearningPath defaultPath = learningPathRepository.findByPositionIdAndIsDefault(positionId, true)
                .orElseThrow(() -> new ResourceNotFoundException("未找到此职位的标准学习路径。"));

        Optional<UserLearningStatus> statusOptional = userLearningStatusRepository.findById(userId);

        if (statusOptional.isPresent()) {
            UserLearningStatus existingStatus = statusOptional.get();
            existingStatus.setActivePath(defaultPath);
            existingStatus.setUpdatedAt(LocalDateTime.now());
            userLearningStatusRepository.save(existingStatus);
        } else {
            UserLearningStatus newStatus = new UserLearningStatus();
            newStatus.setUser(userRepository.getReferenceById(userId));
            newStatus.setActivePath(defaultPath);
            newStatus.setUpdatedAt(LocalDateTime.now());
            userLearningStatusRepository.save(newStatus);
        }
    }
}
package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.PositionMapper;
import com.lin.kglsys.application.service.PositionService;
import com.lin.kglsys.common.exception.business.InvalidParameterException;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.domain.entity.Position;
import com.lin.kglsys.domain.entity.User;
import com.lin.kglsys.domain.entity.UserTargetPosition;
import com.lin.kglsys.domain.valobj.UserTargetSource;
import com.lin.kglsys.dto.response.PositionDTO;
import com.lin.kglsys.dto.response.RecommendedPositionDTO;
import com.lin.kglsys.infra.repository.PositionRepository;
import com.lin.kglsys.infra.repository.UserRepository;
import com.lin.kglsys.infra.repository.UserTargetPositionRepository;
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

        // 增加存在性检查
        Optional<UserTargetPosition> existingTarget = userTargetPositionRepository.findByUserIdAndPositionId(userId, positionId);
        if (existingTarget.isPresent()) {
            // 如果已经存在，可以直接返回成功，或者抛出业务异常提示用户
            throw new InvalidParameterException("您已选择该岗位作为目标，请勿重复添加。");
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("要选择的岗位不存在"));

        // 简单实现：直接添加。未来可优化为检查是否已存在。
        UserTargetPosition target = new UserTargetPosition();
        target.setUser(user);
        target.setPosition(position);
        target.setSource(UserTargetSource.MANUAL); // 标记为手动选择
        target.setCreatedAt(LocalDateTime.now());
        // 手动选择的岗位，匹配分可以为null

        userTargetPositionRepository.save(target);
    }
}
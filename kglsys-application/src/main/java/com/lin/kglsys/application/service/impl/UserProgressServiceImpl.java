package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.service.UserProgressService;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.domain.entity.LearningPathNode;
import com.lin.kglsys.domain.entity.User;
import com.lin.kglsys.domain.entity.UserLearningProgress;
import com.lin.kglsys.domain.valobj.UserLearningProgressStatus;
import com.lin.kglsys.infra.repository.LearningPathNodeRepository;
import com.lin.kglsys.infra.repository.UserLearningProgressRepository;
import com.lin.kglsys.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserProgressServiceImpl implements UserProgressService {

    private final UserLearningProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final LearningPathNodeRepository nodeRepository;

    @Override
    @Transactional
    public void updateNodeProgress(Long nodeId, UserLearningProgressStatus newStatus) {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) throw new UserNotFoundException();

        // 查找现有的进度记录，如果不存在则创建一个新的
        UserLearningProgress progress = progressRepository.findByUserIdAndPathNodeId(userId, nodeId)
                .orElseGet(() -> createNewProgressRecord(userId, nodeId));

        // 更新状态和时间戳
        progress.setStatus(newStatus);
        progress.setUpdatedAt(LocalDateTime.now());

        if (newStatus == UserLearningProgressStatus.IN_PROGRESS && progress.getStartedAt() == null) {
            progress.setStartedAt(LocalDateTime.now());
        } else if (newStatus == UserLearningProgressStatus.MASTERED) {
            progress.setCompletedAt(LocalDateTime.now());
        }

        progressRepository.save(progress);
    }

    private UserLearningProgress createNewProgressRecord(Long userId, Long nodeId) {
        User user = userRepository.getReferenceById(userId);
        LearningPathNode node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new ResourceNotFoundException("学习节点不存在"));

        UserLearningProgress newProgress = new UserLearningProgress();
        newProgress.setUser(user);
        newProgress.setPathNode(node);
        return newProgress;
    }
}
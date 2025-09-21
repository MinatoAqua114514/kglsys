package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.AdminUserProgressMapper;
import com.lin.kglsys.application.service.AdminUserProgressService;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.domain.entity.LearningPath;
import com.lin.kglsys.domain.entity.UserLearningProgress;
import com.lin.kglsys.domain.entity.UserLearningStatus;
import com.lin.kglsys.dto.response.UserProgressDetailDTO;
import com.lin.kglsys.dto.response.UserProgressNodeDTO;
import com.lin.kglsys.infra.repository.UserLearningProgressRepository;
import com.lin.kglsys.infra.repository.UserLearningStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserProgressServiceImpl implements AdminUserProgressService {

    private final UserLearningStatusRepository userLearningStatusRepository;
    private final UserLearningProgressRepository userProgressRepository;
    private final AdminUserProgressMapper userProgressMapper;

    @Override
    @Transactional(readOnly = true)
    public UserProgressDetailDTO getUserProgressDetail(Long userId) {
        UserLearningStatus status = userLearningStatusRepository.findByIdWithDetails(userId)
                .orElseThrow(UserNotFoundException::new);

        UserProgressDetailDTO detailDTO = new UserProgressDetailDTO();
        detailDTO.setUserId(status.getUser().getId());
        detailDTO.setUserEmail(status.getUser().getEmail());
        detailDTO.setUserNickname(status.getUser().getUserProfile().getNickname());

        LearningPath activePath = status.getActivePath();
        if (activePath == null) {
            throw new ResourceNotFoundException("该用户尚未选择学习路径");
        }

        detailDTO.setActivePathId(activePath.getId());
        detailDTO.setActivePathName(activePath.getName());

        List<UserLearningProgress> progresses = userProgressRepository.findUserProgressForPath(userId, activePath.getId());
        List<UserProgressNodeDTO> nodeDTOs = progresses.stream()
                .map(userProgressMapper::toUserProgressNodeDTO)
                .collect(Collectors.toList());

        detailDTO.setProgressNodes(nodeDTOs);

        return detailDTO;
    }
}
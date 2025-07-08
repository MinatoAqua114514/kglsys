package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.UserProfileMapper;
import com.lin.kglsys.application.service.UserProfileService;
import com.lin.kglsys.common.exception.business.DuplicateUsernameException;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.domain.entity.User;
import com.lin.kglsys.domain.entity.UserProfile;
import com.lin.kglsys.dto.request.UpdateUserProfileRequest;
import com.lin.kglsys.dto.response.UserProfileResponse;
import com.lin.kglsys.infra.repository.UserProfileRepository;
import com.lin.kglsys.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile() {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) {
            throw new UserNotFoundException(); // 或者更具体的未认证异常
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return userProfileMapper.toResponse(userProfile, user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(UpdateUserProfileRequest request) {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) {
            throw new UserNotFoundException();
        }

        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 检查昵称唯一性
        checkNicknameUniqueness(request.getNickname(), userId);

        // 使用MapStruct更新实体
        userProfileMapper.updateEntityFromDto(request, userProfile);

        // 保存更新后的实体
        UserProfile updatedProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toResponse(updatedProfile, userProfile.getUser());
    }

    /**
     * 检查昵称是否唯一。
     * @param newNickname 新的昵称
     * @param currentUserId 当前用户的ID
     */
    private void checkNicknameUniqueness(String newNickname, Long currentUserId) {
        userProfileRepository.findByNickname(newNickname)
                .ifPresent(profile -> {
                    // 如果找到了一个同名昵称，并且这个昵称不属于当前用户，则抛出异常
                    if (!Objects.equals(profile.getUser().getId(), currentUserId)) {
                        throw new DuplicateUsernameException();
                    }
                });
    }
}
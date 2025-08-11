package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.AdminMapper;
import com.lin.kglsys.application.mapper.UserProfileMapper;
import com.lin.kglsys.application.service.AdminService;
import com.lin.kglsys.common.constant.MailConstants;
import com.lin.kglsys.common.exception.business.DuplicateUsernameException;
import com.lin.kglsys.common.exception.business.InvalidParameterException;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.domain.entity.User;
import com.lin.kglsys.domain.entity.UserProfile;
import com.lin.kglsys.domain.valobj.UserRole;
import com.lin.kglsys.dto.request.UpdateUserProfileRequest;
import com.lin.kglsys.dto.response.AdminUserViewDTO;
import com.lin.kglsys.dto.response.UserProfileResponse;
import com.lin.kglsys.infra.repository.UserProfileRepository;
import com.lin.kglsys.infra.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final AdminMapper adminMapper;
    private final UserProfileMapper userProfileMapper;
    private final PasswordEncoder passwordEncoder;
    private final AmqpTemplate amqpTemplate;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminUserViewDTO> listUsers(Pageable pageable, String email, String nickname) {
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(email)) {
                predicates.add(cb.like(root.get("email"), "%" + email + "%"));
            }
            if (StringUtils.hasText(nickname)) {
                // 需要连接查询 UserProfile
                predicates.add(cb.like(root.join("userProfile").get("nickname"), "%" + nickname + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<User> userPage = userRepository.findAll(spec, pageable);
        return userPage.map(adminMapper::toAdminUserViewDTO);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 业务规则：不能禁用最后一个激活的管理员
        if (!enabled && user.getRoles().stream().anyMatch(role -> role.getName().equals(UserRole.ADMIN.name()))) {
            long activeAdminCount = userRepository.countByRoleNameAndEnabled(UserRole.ADMIN.name(), true);
            if (activeAdminCount <= 1 && Objects.equals(user.getId(), userId)) {
                throw new InvalidParameterException("无法禁用系统中最后一个有效的管理员账户");
            }
        }

        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetUserPassword(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 1. 生成一个安全的新随机密码
        String newPassword = RandomStringUtils.randomAlphanumeric(10);

        // 2. 加密新密码并更新用户
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 3. 发送邮件通知用户
        Map<String, Object> data = Map.of(
                "type", "admin_reset",
                "email", user.getEmail(),
                "code", newPassword // 复用code字段传递新密码
        );
        amqpTemplate.convertAndSend(MailConstants.MQ_EMAIL_QUEUE, data);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfileById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException()); // 理论上用户存在，profile就应该存在

        return userProfileMapper.toResponse(userProfile, user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserProfileById(Long userId, UpdateUserProfileRequest request) {
        // 1. 验证目标用户是否存在
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        // 2. 检查昵称唯一性（不能与其他用户的昵称冲突）
        checkNicknameUniqueness(request.getNickname(), userId);

        // 3. 使用MapStruct更新实体
        userProfileMapper.updateEntityFromDto(request, userProfile);

        // 4. 保存并返回更新后的结果
        UserProfile updatedProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.toResponse(updatedProfile, updatedProfile.getUser());
    }

    /**
     * 检查昵称是否被其他用户占用
     * @param newNickname 新昵称
     * @param userIdToUpdate 正在被更新的用户ID
     */
    private void checkNicknameUniqueness(String newNickname, Long userIdToUpdate) {
        userProfileRepository.findByNickname(newNickname)
                .ifPresent(profile -> {
                    // 如果找到了一个同名昵称，并且这个昵称不属于当前正在修改的用户，则抛出异常
                    if (!Objects.equals(profile.getUser().getId(), userIdToUpdate)) {
                        throw new DuplicateUsernameException();
                    }
                });
    }
}
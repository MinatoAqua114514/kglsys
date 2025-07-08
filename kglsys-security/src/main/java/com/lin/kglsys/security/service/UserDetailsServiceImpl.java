package com.lin.kglsys.security.service;

import com.lin.kglsys.domain.entity.UserProfile;
import com.lin.kglsys.infra.repository.UserProfileRepository;
import com.lin.kglsys.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    // 简单的邮箱格式校验正则表达式
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 判断输入的是邮箱还是昵称
        if (EMAIL_PATTERN.matcher(username).matches()) {
            // [修正] 使用新的查询方法，确保roles被加载
            return userRepository.findByEmailWithRoles(username)
                    .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        } else {
            // [修正] 使用新的查询方法，确保user和roles都被加载
            return userProfileRepository.findByNicknameWithUserAndRoles(username)
                    .map(UserProfile::getUser)
                    .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
        }
    }
}
package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.service.AuthService;
import com.lin.kglsys.common.constant.MailConstants;
import com.lin.kglsys.common.constant.RedisConstants;
import com.lin.kglsys.common.constant.UserProfileConstants;
import com.lin.kglsys.common.exception.business.DuplicateUsernameException;
import com.lin.kglsys.common.exception.business.InvalidParameterException;
import com.lin.kglsys.common.exception.business.PermissionDeniedException;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.domain.entity.Role;
import com.lin.kglsys.domain.entity.User;
import com.lin.kglsys.domain.entity.UserProfile;
import com.lin.kglsys.domain.valobj.UserRole;
import com.lin.kglsys.dto.request.ResetPasswordRequest;
import com.lin.kglsys.dto.request.UserLoginRequest;
import com.lin.kglsys.dto.response.JwtResponse;
import com.lin.kglsys.dto.request.UserRegisterRequest;
import com.lin.kglsys.infra.repository.RoleRepository;
import com.lin.kglsys.infra.repository.UserProfileRepository;
import com.lin.kglsys.infra.repository.UserRepository;
import com.lin.kglsys.security.jwt.JwtTokenProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor // 使用Lombok自动注入final字段
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserProfileRepository userProfileRepository;

    @Resource
    AmqpTemplate amqpTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Value("${app.rabbitmq.email-queue}")
    private String emailQueueName;

    /**
     * 用户注册
     * @param request 注册请求DTO
     * @return 生成的Token
     */
    @Override
    @Transactional
    public JwtResponse register(UserRegisterRequest request) {
        // [新增] 1. 校验邮箱验证码
        String key = MailConstants.EMAIL_VERIFY_DATA + request.getEmail();
        String storedCode = stringRedisTemplate.opsForValue().get(key);

        if (storedCode == null) {
            throw new InvalidParameterException("验证码已过期，请重新获取");
        }
        if (!storedCode.equals(request.getVerifyCode())) {
            throw new InvalidParameterException("验证码不正确");
        }

        // 2. 验证邮箱唯一性
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUsernameException();
        }

        // 3. 查找用户选择的角色
        UserRole requestedRole = request.getRole();
        if (requestedRole == UserRole.ADMIN) { // [安全加固]
            throw new PermissionDeniedException();
        }
        Role userRole = roleRepository.findByName(requestedRole.name())
                .orElseThrow(() -> new ResourceNotFoundException("请求的角色不存在: " + requestedRole.name()));

        // 4. 创建并保存用户实体
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);

        // 5. [新增] 自动创建并保存用户个人资料
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(savedUser); // 关联新创建的用户
        // 生成一个6位的随机数作为昵称后缀
        String randomNickname = UserProfileConstants.NICKNAME_PREFIX + (100000 + new Random().nextInt(900000));
        userProfile.setNickname(randomNickname);
        userProfile.setAvatarUrl(UserProfileConstants.DEFAULT_AVATAR_URL);
        userProfileRepository.save(userProfile);

        // 6. 生成JWT令牌
        String token = jwtTokenProvider.generateToken(savedUser);

        // 7. 注册成功后，删除验证码
        stringRedisTemplate.delete(key);

        // 8. 返回包含token的响应
        return new JwtResponse(token);
    }

    /**
     * 用户登录
     * @param request 登录请求DTO
     * @return 生成的Token
     */
    @Override
    @Transactional(readOnly = true)
    public JwtResponse login(UserLoginRequest request) {
        // 1. 使用AuthenticationManager进行认证
        // 这会触发 UserDetailsServiceImpl.loadUserByUsername 方法
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. 将认证信息存入SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. 从认证信息中获取User对象
        User user = (User) authentication.getPrincipal();

        // 4. 生成JWT
        String token = jwtTokenProvider.generateToken(user);

        // 5. 返回响应
        return new JwtResponse(token);
    }

    /**
     * 用户忘记密码，申请获取重置密码的邮箱验证码
     * @param email 用户邮箱
     */
    @Override
    public void forgotPassword(String email) {
        // 1. 检查用户是否存在
        userRepository.findByEmail(email).ifPresent(user -> {
            // 2. 生成一个安全的、唯一的重置令牌
            String resetToken = UUID.randomUUID().toString();

            // 3. 将令牌与邮箱的映射关系存入Redis，有效期设置为15分钟
            String key = emailQueueName + "reset:" + resetToken;
            stringRedisTemplate.opsForValue().set(key, email, 15, TimeUnit.MINUTES);

            // 4. 将包含重置令牌的邮件任务发送到RabbitMQ
            // 注意：实际应用中，邮件内容应包含一个前端页面的链接，如 /reset-password?token=xxxx
            Map<String, Object> data = Map.of(
                    "type", "forget",
                    "email", email,
                    "code", resetToken // 此处复用code字段传递token
            );
            amqpTemplate.convertAndSend(MailConstants.MQ_EMAIL_QUEUE, data);
        });
        // 如果用户不存在，我们静默处理，不给外部任何提示，防止邮箱探测
    }

    /**
     * 使用新密码和验证码进行重置密码
     * @param request 包含令牌和新密码的请求DTO
     */
    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // 1. 从Redis中验证重置令牌
        String key = emailQueueName + "reset:" + request.getVerifyCode();
        String email = stringRedisTemplate.opsForValue().get(key);

        if (email == null) {
            throw new InvalidParameterException("密码重置链接无效或已过期");
        }

        // 2. 根据邮箱查找用户
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("无法找到与重置令牌关联的用户"));

        // 3. 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 4. 使重置令牌失效（从Redis中删除）
        stringRedisTemplate.delete(key);
    }

    /**
     * 【优化后】生成验证码存入Redis，并将邮件发送请求提交到消息队列等待发送
     * 增加了IP限流防刷机制。
     * @param type 验证码用途 (register/forget)
     * @param email 邮件地址
     */
    @Override
    public void registerEmailVerifyCode(String type, String email) {
        // 1. 【新增】获取请求IP地址
        String ip = getRequestIpAddress();
        String ipKey = RedisConstants.EMAIL_VERIFY_IP_LIMIT + ip;

        // 2. 【新增】检查此IP是否在限流期内
        if (stringRedisTemplate.hasKey(ipKey)) {
            // 如果key存在，说明60秒内已请求过，抛出异常
            throw new InvalidParameterException("请求过于频繁，请稍后再试");
        }

        // 3. 【新增】注册前置检查：如果是注册请求，需要确保邮箱未被注册
        if ("register".equals(type) && userRepository.existsByEmail(email)) {
            throw new DuplicateUsernameException(); // 复用已有的异常
        }

        // 4. 生成4位随机验证码
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;

        // 5. 将验证码存入Redis，有效期3分钟
        String dataKey = MailConstants.EMAIL_VERIFY_DATA + email;
        stringRedisTemplate.opsForValue().set(dataKey, String.valueOf(code), 3, TimeUnit.MINUTES);

        // 6. 将IP地址存入Redis作为限流标记，有效期60秒
        stringRedisTemplate.opsForValue().set(ipKey, "locked", 60, TimeUnit.SECONDS);

        // 7. 只有在所有Redis操作成功后，才发送邮件任务到RabbitMQ
        try {
            Map<String, Object> data = Map.of("type", type, "email", email, "code", code);
            amqpTemplate.convertAndSend(MailConstants.MQ_EMAIL_QUEUE, data);
        } catch (Exception e) {
            // 如果消息队列发送失败，这是一个严重问题，需要记录并可能需要回滚Redis操作（尽管在此场景下影响较小）
            log.error("发送邮件任务到RabbitMQ失败，但Redis数据已写入。Email: {}", email, e);
            // 为了保持一致性，可以选择删除刚刚写入的Redis键
            stringRedisTemplate.delete(dataKey);
            stringRedisTemplate.delete(ipKey);
            // 重新抛出异常，让全局异常处理器捕获
            throw e;
        }

        // 8. 【新增】将IP地址存入Redis作为限流标记，有效期60秒
        stringRedisTemplate.opsForValue().set(ipKey, "locked", 60, TimeUnit.SECONDS);

        log.info("已为邮箱 [{}] 生成类型为 [{}] 的验证码，请求IP: {}", email, type, ip);
    }

    /**
     * 【新增】从当前请求上下文中获取IP地址的辅助方法
     * @return IP地址或"unknown"
     */
    private String getRequestIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 优先从X-Forwarded-For头获取（处理Nginx等反向代理情况）
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            // 如果通过多个代理，X-Forwarded-For的值可能是 "client, proxy1, proxy2"，取第一个非unknown的IP
            return ip.split(",")[0].trim();
        }
        return "unknown";
    }
}
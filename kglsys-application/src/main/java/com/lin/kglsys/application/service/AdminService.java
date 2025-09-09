package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.UpdateUserProfileRequest;
import com.lin.kglsys.dto.response.AdminUserAnswerViewDTO;
import com.lin.kglsys.dto.response.AdminUserViewDTO;
import com.lin.kglsys.dto.response.UserProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    /**
     * 分页并按条件搜索用户列表
     * @param pageable 分页参数
     * @param email 可选的邮箱过滤条件
     * @param nickname 可选的昵称过滤条件
     * @return 分页的用户视图DTO
     */
    Page<AdminUserViewDTO> listUsers(Pageable pageable, String email, String nickname);

    /**
     * 更新用户状态（启用/禁用）
     * @param userId 用户ID
     * @param enabled 新的状态
     */
    void updateUserStatus(Long userId, boolean enabled);

    /**
     * 为用户重置密码
     * @param userId 用户ID
     */
    void resetUserPassword(Long userId);

    /**
     * 管理员根据ID获取用户个人资料
     * @param userId 用户ID
     * @return 用户个人资料DTO
     */
    UserProfileResponse getUserProfileById(Long userId);

    /**
     * 管理员根据ID更新用户个人资料
     * @param userId 用户ID
     * @param request 更新请求DTO
     * @return 更新后的用户个人资料DTO
     */
    UserProfileResponse updateUserProfileById(Long userId, UpdateUserProfileRequest request);

    /**
     * 分页并按条件搜索用户答卷记录
     * @param pageable 分页参数
     * @param userEmail 可选的用户邮箱过滤条件
     * @param questionText 可选的题干内容过滤条件
     * @return 分页的用户答卷视图DTO
     */
    Page<AdminUserAnswerViewDTO> listUserAnswers(Pageable pageable, String userEmail, String questionText);
}
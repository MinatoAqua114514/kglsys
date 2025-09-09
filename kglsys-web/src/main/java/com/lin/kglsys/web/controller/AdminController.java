package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.UpdateUserProfileRequest;
import com.lin.kglsys.dto.request.UpdateUserStatusRequest;
import com.lin.kglsys.dto.response.AdminUserAnswerViewDTO;
import com.lin.kglsys.dto.response.AdminUserViewDTO;
import com.lin.kglsys.dto.response.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 获取用户列表（分页、可搜索）
     * @param pageable 分页参数 (e.g., ?page=0&size=10&sort=createdAt,desc)
     * @param email (可选) 按邮箱模糊查询
     * @param nickname (可选) 按昵称模糊查询
     * @return 分页的用户信息
     */
    @GetMapping("/users")
    public ApiResult<Page<AdminUserViewDTO>> getUsers(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nickname) {
        Page<AdminUserViewDTO> userPage = adminService.listUsers(pageable, email, nickname);
        return ApiResult.success(userPage);
    }

    /**
     * 启用或禁用用户
     * @param userId 用户ID
     * @param request 请求体，包含 enabled 状态
     * @return 成功响应
     */
    @PutMapping("/users/{userId}/status")
    public ApiResult<Void> updateUserStatus(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        adminService.updateUserStatus(userId, request.getEnabled());
        return ApiResult.success();
    }

    /**
     * 管理员重置用户密码
     * @param userId 用户ID
     * @return 成功响应
     */
    @PostMapping("/users/{userId}/reset-password")
    public ApiResult<Void> resetUserPassword(@PathVariable Long userId) {
        adminService.resetUserPassword(userId);
        return ApiResult.success();
    }

    /**
     * 管理员获取指定用户的个人资料
     * @param userId 用户ID
     * @return 用户的详细个人资料
     */
    @GetMapping("/users/{userId}/profile")
    public ApiResult<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
        UserProfileResponse userProfile = adminService.getUserProfileById(userId);
        return ApiResult.success(userProfile);
    }

    /**
     * 管理员更新指定用户的个人资料
     * @param userId 用户ID
     * @param request 包含待更新信息的请求体
     * @return 更新后的用户个人资料
     */
    @PutMapping("/users/{userId}/profile")
    public ApiResult<UserProfileResponse> updateUserProfile(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        UserProfileResponse updatedProfile = adminService.updateUserProfileById(userId, request);
        return ApiResult.success(updatedProfile);
    }

    /**
     * 获取用户答卷列表（分页、可搜索）
     * @param pageable 分页参数 (e.g., ?page=0&size=10&sort=createdAt,desc)
     * @param userEmail (可选) 按用户邮箱模糊查询
     * @param questionText (可选) 按题干内容模糊查询
     * @return 分页的用户答卷信息
     */
    @GetMapping("/assessment-answers")
    public ApiResult<Page<AdminUserAnswerViewDTO>> getAssessmentAnswers(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String questionText) {
        Page<AdminUserAnswerViewDTO> answerPage = adminService.listUserAnswers(pageable, userEmail, questionText);
        return ApiResult.success(answerPage);
    }
}
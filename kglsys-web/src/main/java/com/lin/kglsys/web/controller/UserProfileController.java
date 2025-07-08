package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.UserProfileService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.UpdateUserProfileRequest;
import com.lin.kglsys.dto.response.UserProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * 获取当前登录用户的个人信息
     * @return 包含用户信息的统一响应体
     */
    @GetMapping("/profile")
    public ApiResult<UserProfileResponse> getUserProfile() {
        UserProfileResponse userProfile = userProfileService.getUserProfile();
        return ApiResult.success(userProfile);
    }

    /**
     * 更新当前登录用户的个人信息
     * @param request 包含待更新信息的请求体
     * @return 包含更新后用户信息的统一响应体
     */
    @PutMapping("/profile")
    public ApiResult<UserProfileResponse> updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        UserProfileResponse updatedProfile = userProfileService.updateUserProfile(request);
        return ApiResult.success(updatedProfile);
    }
}
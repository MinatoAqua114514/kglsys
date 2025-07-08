package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.UpdateUserProfileRequest;
import com.lin.kglsys.dto.response.UserProfileResponse;

public interface UserProfileService {

    /**
     * 获取当前登录用户的个人信息
     * @return 用户个人信息响应DTO
     */
    UserProfileResponse getUserProfile();

    /**
     * 更新当前登录用户的个人信息
     * @param request 包含待更新信息的请求DTO
     * @return 更新后的用户个人信息响应DTO
     */
    UserProfileResponse updateUserProfile(UpdateUserProfileRequest request);
}
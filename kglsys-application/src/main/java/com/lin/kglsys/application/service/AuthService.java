package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.ResetPasswordRequest;
import com.lin.kglsys.dto.request.UserLoginRequest;
import com.lin.kglsys.dto.response.JwtResponse;
import com.lin.kglsys.dto.request.UserRegisterRequest;

public interface AuthService {
    /**
     * 处理用户注册业务
     * @param request 注册请求DTO
     * @return 包含JWT的响应DTO
     */
    JwtResponse register(UserRegisterRequest request);

    /**
     * 处理用户登录业务
     * @param request 登录请求DTO
     * @return 包含JWT的响应DTO
     */
    JwtResponse login(UserLoginRequest request);

    /**
     * 处理忘记密码请求
     * @param email 用户邮箱
     */
    void forgotPassword(String email);

    /**
     * 发送邮箱验证码用于用户注册/重置密码
     * @param type 验证码用于注册/重置密码
     * @param email 用户邮箱
     */
    void registerEmailVerifyCode(String type, String email);

    /**
     * 根据重置令牌重置用户密码
     * @param request 包含令牌和新密码的请求DTO
     */
    void resetPassword(ResetPasswordRequest request);
}
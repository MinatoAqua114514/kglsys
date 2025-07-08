package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AuthService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.ForgotPasswordRequest;
import com.lin.kglsys.dto.request.ResetPasswordRequest;
import com.lin.kglsys.dto.request.UserLoginRequest;
import com.lin.kglsys.dto.response.JwtResponse;
import com.lin.kglsys.dto.request.UserRegisterRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户注册接口
     * @param request 包含邮箱、密码和角色的请求体
     * @return 包含JWT的统一响应体
     */
    @PostMapping("/register")
    public ApiResult<JwtResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        JwtResponse jwtResponse = authService.register(request);
        return ApiResult.success(jwtResponse);
    }

    /**
     * 用户登录接口
     * @param request 包含用户名(邮箱或昵称)和密码的请求体
     * @return 包含JWT的统一响应体
     */
    @PostMapping("/login")
    public ApiResult<JwtResponse> login(@Valid @RequestBody UserLoginRequest request) {
        JwtResponse jwtResponse = authService.login(request);
        return ApiResult.success(jwtResponse);
    }

    /**
     * 忘记密码接口
     * (当前仅接收请求，具体逻辑待实现)
     */
    @PostMapping("/forgot-password")
    public ApiResult<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        // 无论用户是否存在，都返回成功，防止信息泄露
        return ApiResult.success();
    }

    /**
     * 重置密码接口
     */
    @PostMapping("/reset-password")
    public ApiResult<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResult.success();
    }

    /**
     * 请求验证码接口
     * @param email 邮箱
     * @param type  类型 (register 或 forget)
     * @return 统一成功响应
     */
    @GetMapping("/ask-code")
    public ApiResult<Void> askCode(
            @RequestParam @Email(message = "邮箱格式不正确") String email,
            @RequestParam @Pattern(regexp = "^(register|forget)$", message = "无效的请求类型") String type) {

        authService.registerEmailVerifyCode(type, email);
        return ApiResult.success();
    }
}
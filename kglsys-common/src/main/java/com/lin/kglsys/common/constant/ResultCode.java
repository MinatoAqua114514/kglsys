package com.lin.kglsys.common.constant;

import lombok.Getter;

/**
 * 业务状态码枚举
 */
@Getter
public enum ResultCode {
    // --- 通用成功 ---
    SUCCESS(200, "操作成功"),

    // --- 通用失败 ---
    FAILURE(500, "操作失败"),
    SYSTEM_ERROR(500, "系统内部错误，请联系管理员"),

    // --- 参数校验失败 (4xx) ---
    PARAM_VALIDATION_ERROR(400, "参数校验失败"),
    RESOURCE_NOT_FOUND(404, "请求的资源不存在"),

    // --- 认证与授权 (4xx) ---
    UNAUTHORIZED(401, "用户未认证，请先登录"),
    AUTHENTICATION_FAILED(401, "用户名或密码错误"),
    TOKEN_INVALID(401, "无效的访问令牌"),
    TOKEN_EXPIRED(401, "访问令牌已过期"),
    PERMISSION_DENIED(403, "权限不足，禁止访问"),

    // --- 业务逻辑错误 (1000+) ---
    USER_NOT_FOUND(1001, "用户不存在"),
    DUPLICATE_USERNAME(1002, "用户名已存在"),
    PROBLEM_NOT_FOUND(1003, "编程题目不存在"),
    SUBMISSION_NOT_FOUND(1004, "提交记录不存在"),
    CODE_SUBMISSION_FAILED(1005, "代码提交失败，请稍后重试"),
    EXTERNAL_SERVICE_ERROR(1006, "外部服务调用异常");


    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
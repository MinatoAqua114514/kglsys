package com.lin.kglsys.common.response;

import com.lin.kglsys.common.constant.ResultCode;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 全局统一API响应体
 * @param <T> 响应数据的类型
 */
@Getter
public class ApiResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 业务状态码
     */
    private final int code;

    /**
     * 响应消息
     */
    private final String message;

    /**
     * 响应数据
     */
    private final T data;

    private ApiResult(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    private ApiResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private ApiResult(ResultCode resultCode, String message) {
        this.code = resultCode.getCode();
        this.message = message;
        this.data = null;
    }

    /**
     * 成功响应 - 无数据
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(ResultCode.SUCCESS, null);
    }

    /**
     * 成功响应 - 有数据
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(ResultCode.SUCCESS, data);
    }

    /**
     * 失败响应 - 使用预定义ResultCode
     */
    public static <T> ApiResult<T> failure(ResultCode resultCode) {
        return new ApiResult<>(resultCode, null);
    }

    /**
     * 失败响应 - 使用预定义ResultCode并附带数据
     */
    public static <T> ApiResult<T> failure(ResultCode resultCode, T data) {
        return new ApiResult<>(resultCode, data);
    }

    /**
     *  失败响应 - 使用预定义ResultCode并附带消息
     */
    public static <T> ApiResult<T> failure(ResultCode resultCode, String message) {
        return new ApiResult<>(resultCode, message);
    }

    /**
     * 失败响应 - 自定义消息
     */
    public static <T> ApiResult<T> failure(String message) {
        return new ApiResult<>(ResultCode.FAILURE.getCode(), message, null);
    }
}
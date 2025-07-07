package com.lin.kglsys.common.handler;

import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.BaseException;
import com.lin.kglsys.common.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK) // 返回200，业务错误码在body中体现
    public ApiResult<?> handleBaseException(BaseException ex) {
        log.error("业务异常: {}", ex.getMessage());
        return ApiResult.failure(ex.getResultCode());
    }

    /**
     * 处理@Valid注解校验失败的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 参数错误，返回400
    public ApiResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", errorMessage);
        return ApiResult.failure(ResultCode.PARAM_VALIDATION_ERROR, errorMessage);
    }

    /**
     * 处理其他所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 系统级错误，返回500
    public ApiResult<?> handleAllUncaughtException(Exception ex) {
        log.error("未捕获的系统异常", ex);
        return ApiResult.failure(ResultCode.SYSTEM_ERROR);
    }
}
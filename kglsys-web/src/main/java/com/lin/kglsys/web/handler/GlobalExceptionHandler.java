package com.lin.kglsys.web.handler;

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
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.amqp.AmqpException;

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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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

    /**
     * 处理Redis连接失败异常
     * 这个处理器应该放在通用 DataAccessException 处理器之前
     */
    @ExceptionHandler(RedisConnectionFailureException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // 返回503 Service Unavailable
    public ApiResult<?> handleRedisConnectionFailureException(RedisConnectionFailureException ex) {
        log.error("缓存服务连接失败: {}", ex.getMessage());
        return ApiResult.failure(ResultCode.CACHE_SERVICE_ERROR);
    }

    /**
     * 处理所有Spring AMQP相关的运行时异常
     * AmqpException是所有AMQP相关异常的根类，可以统一捕获。
     */
    @ExceptionHandler(AmqpException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiResult<?> handleAmqpException(AmqpException ex) {
        log.error("消息队列服务异常: {}", ex.getMessage());
        return ApiResult.failure(ResultCode.MQ_SERVICE_ERROR);
    }
}
package com.lin.kglsys.common.exception;

import com.lin.kglsys.common.constant.ResultCode;
import lombok.Getter;

/**
 * 自定义业务异常基类
 */
@Getter
public class BaseException extends RuntimeException {

    private final ResultCode resultCode;

    public BaseException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BaseException(String message) {
        super(message);
        this.resultCode = ResultCode.FAILURE;
    }
}
package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.BaseException;

public class TokenValidationException extends BaseException {
    public TokenValidationException(ResultCode resultCode) {
        super(resultCode); // 可以是TOKEN_INVALID或TOKEN_EXPIRED
    }
}

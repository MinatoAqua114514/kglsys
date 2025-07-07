package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.BaseException;

public class AuthenticationFailedException extends BaseException {
    public AuthenticationFailedException() {
        super(ResultCode.AUTHENTICATION_FAILED);
    }
}
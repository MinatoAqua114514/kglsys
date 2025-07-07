package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException() {
        super(ResultCode.USER_NOT_FOUND.getMessage());
    }
}
package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.BaseException;

public class DuplicateUsernameException extends BaseException {
    public DuplicateUsernameException() {
        super(ResultCode.DUPLICATE_USERNAME);
    }
}
package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.BaseException;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException() {
        super(ResultCode.RESOURCE_NOT_FOUND);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

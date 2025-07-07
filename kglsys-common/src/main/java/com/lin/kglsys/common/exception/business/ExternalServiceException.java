package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.BaseException;

public class ExternalServiceException extends BaseException {
    public ExternalServiceException() {
        super(ResultCode.EXTERNAL_SERVICE_ERROR);
    }
    public ExternalServiceException(String message) {
        super(message);
    }
}

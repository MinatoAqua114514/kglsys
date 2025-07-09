package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.BaseException;

public class SubmissionNotFoundException extends BaseException {
    public SubmissionNotFoundException(ResultCode resultCode) {
        super(resultCode);
    }
}

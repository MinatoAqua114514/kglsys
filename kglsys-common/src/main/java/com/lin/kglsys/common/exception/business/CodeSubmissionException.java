package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.BaseException;

public class CodeSubmissionException extends BaseException {
    public CodeSubmissionException() {
        super(ResultCode.CODE_SUBMISSION_FAILED);
    }
}
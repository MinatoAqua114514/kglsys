package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;

public class ProblemNotFoundException extends ResourceNotFoundException {
    public ProblemNotFoundException() {
        super(ResultCode.PROBLEM_NOT_FOUND.getMessage());
    }
}
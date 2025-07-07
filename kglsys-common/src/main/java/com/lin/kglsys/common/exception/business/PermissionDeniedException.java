package com.lin.kglsys.common.exception.business;

import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.BaseException;

public class PermissionDeniedException extends BaseException {
    public PermissionDeniedException() {
        super(ResultCode.PERMISSION_DENIED);
    }
}
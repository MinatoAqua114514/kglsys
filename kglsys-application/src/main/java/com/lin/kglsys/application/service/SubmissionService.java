package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.CodeSubmissionRequest;
import com.lin.kglsys.dto.response.CodeSubmissionResponse;

public interface SubmissionService {
    /**
     * 处理代码提交请求
     * @param request 包含代码、题目ID等信息的请求
     * @return 包含唯一提交ID的响应
     */
    CodeSubmissionResponse submitCode(CodeSubmissionRequest request);
}
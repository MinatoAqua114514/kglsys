package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.SubmissionService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.CodeSubmissionRequest;
import com.lin.kglsys.dto.response.CodeSubmissionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public ApiResult<CodeSubmissionResponse> submitCode(@Valid @RequestBody CodeSubmissionRequest request) {
        CodeSubmissionResponse response = submissionService.submitCode(request);
        return ApiResult.success(response);
    }
}
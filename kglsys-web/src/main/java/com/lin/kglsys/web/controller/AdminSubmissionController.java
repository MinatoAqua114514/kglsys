package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminSubmissionService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.response.SubmissionViewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/submissions")
@RequiredArgsConstructor
public class AdminSubmissionController {

    private final AdminSubmissionService submissionService;

    @GetMapping
    public ApiResult<Page<SubmissionViewDTO>> listSubmissions(
            @PageableDefault(sort = "submittedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String problemTitle) {
        return ApiResult.success(submissionService.listSubmissions(pageable, userEmail, problemTitle));
    }
}
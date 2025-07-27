package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.ProblemService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.response.ProblemDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/problems")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    /**
     * 获取题目详情
     * @param id 题目ID
     * @return 包含题目详情的统一响应体
     */
    @GetMapping("/{id}")
    public ApiResult<ProblemDetailDTO> getProblemDetails(@PathVariable("id") Long id) {
        ProblemDetailDTO details = problemService.getProblemDetails(id);
        return ApiResult.success(details);
    }
}
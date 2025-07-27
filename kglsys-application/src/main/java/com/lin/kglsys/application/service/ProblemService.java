package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.response.ProblemDetailDTO;

public interface ProblemService {

    /**
     * 获取题目详情
     * @param problemId 题目ID
     * @return 题目详情DTO
     */
    ProblemDetailDTO getProblemDetails(Long problemId);
}
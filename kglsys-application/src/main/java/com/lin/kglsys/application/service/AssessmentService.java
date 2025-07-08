package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.AssessmentSubmissionRequest;
import com.lin.kglsys.dto.response.QuestionDTO;
import com.lin.kglsys.dto.response.RecommendedPositionDTO;

import java.util.List;

public interface AssessmentService {

    /**
     * 获取测评问卷
     * @return 问卷题目列表
     */
    List<QuestionDTO> getAssessmentQuestions();

    /**
     * 提交测评问卷答案
     *
     * @param request 包含用户答案的请求体
     * @return 匹配的3个岗位分数
     */
    List<RecommendedPositionDTO> submitAssessment(AssessmentSubmissionRequest request);
}
package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AssessmentService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.AssessmentSubmissionRequest;
import com.lin.kglsys.dto.response.QuestionDTO;
import com.lin.kglsys.dto.response.RecommendedPositionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assessment")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    /**
     * 获取测评问卷
     * @return 包含问卷题目列表的统一响应体
     */
    @GetMapping("/questions")
    public ApiResult<List<QuestionDTO>> getAssessmentQuestions() {
        List<QuestionDTO> questions = assessmentService.getAssessmentQuestions();
        return ApiResult.success(questions);
    }

    /**
     * 提交测评结果并立即获取推荐岗位
     * @param request 包含用户答案的请求体
     * @return 包含推荐岗位列表的统一响应体
     */
    @PostMapping("/submit")
    public ApiResult<List<RecommendedPositionDTO>> submitAssessment(@Valid @RequestBody AssessmentSubmissionRequest request) {
        List<RecommendedPositionDTO> recommendations = assessmentService.submitAssessment(request);
        return ApiResult.success(recommendations);
    }
}
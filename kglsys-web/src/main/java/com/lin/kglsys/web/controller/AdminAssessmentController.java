package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminAssessmentService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.AssessmentQuestionSaveDTO;
import com.lin.kglsys.dto.request.UpdateTendenciesRequestDTO;
import com.lin.kglsys.dto.response.PositionTendencyDTO;
import com.lin.kglsys.dto.response.QuestionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/assessment")
@RequiredArgsConstructor
public class AdminAssessmentController {

    private final AdminAssessmentService adminAssessmentService;

    // --- 题目管理 ---
    @GetMapping("/questions")
    public ApiResult<Page<QuestionDTO>> listQuestions(@PageableDefault(sort = "sequence") Pageable pageable) {
        return ApiResult.success(adminAssessmentService.listQuestions(pageable));
    }

    @GetMapping("/questions/{id}")
    public ApiResult<QuestionDTO> getQuestion(@PathVariable Integer id) {
        return ApiResult.success(adminAssessmentService.getQuestionById(id));
    }

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<QuestionDTO> createQuestion(@Valid @RequestBody AssessmentQuestionSaveDTO request) {
        return ApiResult.success(adminAssessmentService.createQuestion(request));
    }

    @PutMapping("/questions/{id}")
    public ApiResult<QuestionDTO> updateQuestion(@PathVariable Integer id, @Valid @RequestBody AssessmentQuestionSaveDTO request) {
        return ApiResult.success(adminAssessmentService.updateQuestion(id, request));
    }

    @DeleteMapping("/questions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult<Void> deleteQuestion(@PathVariable Integer id) {
        adminAssessmentService.deleteQuestion(id);
        return ApiResult.success();
    }

    // --- 选项倾向管理 ---

    /**
     * 获取指定选项的岗位倾向配置
     */
    @GetMapping("/options/{optionId}/tendencies")
    public ApiResult<List<PositionTendencyDTO>> getTendencies(@PathVariable Integer optionId) {
        return ApiResult.success(adminAssessmentService.getTendenciesForOption(optionId));
    }

    /**
     * 更新指定选项的岗位倾向配置
     */
    @PutMapping("/options/{optionId}/tendencies")
    public ApiResult<List<PositionTendencyDTO>> updateTendencies(
            @PathVariable Integer optionId,
            @Valid @RequestBody UpdateTendenciesRequestDTO request) {
        return ApiResult.success(adminAssessmentService.updateTendenciesForOption(optionId, request));
    }
}
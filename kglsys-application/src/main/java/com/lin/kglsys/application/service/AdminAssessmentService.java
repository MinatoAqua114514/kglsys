package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.AssessmentQuestionSaveDTO;
import com.lin.kglsys.dto.request.UpdateTendenciesRequestDTO;
import com.lin.kglsys.dto.response.PositionTendencyDTO;
import com.lin.kglsys.dto.response.QuestionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminAssessmentService {

    /**
     * 分页获取所有问卷题目
     * @param pageable 分页参数
     * @return 分页的问卷题目
     */
    Page<QuestionDTO> listQuestions(Pageable pageable);

    /**
     * 根据ID获取单个问卷题目的详细信息
     * @param questionId 题目ID
     * @return 题目详情DTO
     */
    QuestionDTO getQuestionById(Integer questionId);

    /**
     * 创建一个新的问卷题目及其选项
     * @param request 包含题目和选项信息的DTO
     * @return 创建后的题目详情DTO
     */
    QuestionDTO createQuestion(AssessmentQuestionSaveDTO request);

    /**
     * 更新一个已存在的问卷题目及其选项
     * @param questionId 要更新的题目ID
     * @param request 包含更新信息的DTO
     * @return 更新后的题目详情DTO
     */
    QuestionDTO updateQuestion(Integer questionId, AssessmentQuestionSaveDTO request);

    /**
     * 删除一个问卷题目及其所有关联选项
     * @param questionId 题目ID
     */
    void deleteQuestion(Integer questionId);

    /**
     * 获取指定选项的岗位倾向配置
     * @param optionId 选项ID
     * @return 岗位倾向配置列表
     */
    List<PositionTendencyDTO> getTendenciesForOption(Integer optionId);

    /**
     * 更新指定选项的岗位倾向配置
     * @param optionId 选项ID
     * @param request 包含新配置的请求
     * @return 更新后的岗位倾向配置列表
     */
    List<PositionTendencyDTO> updateTendenciesForOption(Integer optionId, UpdateTendenciesRequestDTO request);
}
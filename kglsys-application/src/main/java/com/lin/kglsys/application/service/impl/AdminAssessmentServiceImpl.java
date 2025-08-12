package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.AdminAssessmentMapper;
import com.lin.kglsys.application.mapper.AssessmentMapper;
import com.lin.kglsys.application.service.AdminAssessmentService;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.domain.entity.AssessmentQuestion;
import com.lin.kglsys.domain.entity.OptionPositionTendency;
import com.lin.kglsys.domain.entity.QuestionOption;
import com.lin.kglsys.dto.request.AssessmentQuestionSaveDTO;
import com.lin.kglsys.dto.request.QuestionOptionSaveDTO;
import com.lin.kglsys.dto.request.UpdateTendenciesRequestDTO;
import com.lin.kglsys.dto.response.PositionTendencyDTO;
import com.lin.kglsys.dto.response.QuestionDTO;
import com.lin.kglsys.infra.repository.AssessmentQuestionRepository;
import com.lin.kglsys.infra.repository.OptionPositionTendencyRepository;
import com.lin.kglsys.infra.repository.PositionRepository;
import com.lin.kglsys.infra.repository.QuestionOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAssessmentServiceImpl implements AdminAssessmentService {

    private final AssessmentQuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final AdminAssessmentMapper adminMapper;
    private final AssessmentMapper assessmentMapper; // 复用查询时的DTO转换
    private final OptionPositionTendencyRepository tendencyRepository;
    private final PositionRepository positionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDTO> listQuestions(Pageable pageable) {
        Page<AssessmentQuestion> questionPage = questionRepository.findAll(pageable);
        // Page.map() 可以方便地将 Page<Entity> 转换为 Page<DTO>
        return questionPage.map(assessmentMapper::toQuestionDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDTO getQuestionById(Integer questionId) {
        AssessmentQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("问卷题目不存在"));
        return assessmentMapper.toQuestionDTO(question);
    }

    @Override
    @Transactional
    public QuestionDTO createQuestion(AssessmentQuestionSaveDTO request) {
        // 1. 转换并保存问题主体
        AssessmentQuestion question = adminMapper.dtoToQuestion(request);
        question.setCreatedAt(LocalDateTime.now());
        AssessmentQuestion savedQuestion = questionRepository.save(question);

        // 2. 转换、关联并保存选项
        Set<QuestionOption> options = request.getOptions().stream().map(optDto -> {
            QuestionOption option = adminMapper.dtoToOption(optDto);
            option.setQuestion(savedQuestion); // 关联到刚保存的问题
            return option;
        }).collect(Collectors.toSet());
        optionRepository.saveAll(options);

        savedQuestion.setOptions(options);
        return assessmentMapper.toQuestionDTO(savedQuestion);
    }

    @Override
    @Transactional
    public QuestionDTO updateQuestion(Integer questionId, AssessmentQuestionSaveDTO request) {
        // 1. 查找现有问题
        AssessmentQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("要更新的问卷题目不存在"));

        // 2. 更新问题主体属性
        adminMapper.updateQuestionFromDto(request, question);

        // 3. 更新选项（核心逻辑：增、删、改）
        updateOptionsForQuestion(question, request.getOptions());

        AssessmentQuestion updatedQuestion = questionRepository.save(question);
        return assessmentMapper.toQuestionDTO(updatedQuestion);
    }

    @Override
    @Transactional
    public void deleteQuestion(Integer questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("要删除的问卷题目不存在");
        }
        // 由于设置了级联删除（CascadeType.ALL），删除问题会自动删除其所有选项
        questionRepository.deleteById(questionId);
    }

    /**
     * 辅助方法：处理选项的增、删、改
     */
    private void updateOptionsForQuestion(AssessmentQuestion question, List<QuestionOptionSaveDTO> optionDTOs) {
        // 将传入的DTO按ID分组，方便查找
        Map<Integer, QuestionOptionSaveDTO> dtoMap = optionDTOs.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(QuestionOptionSaveDTO::getId, Function.identity()));

        // 将数据库中现有的选项按ID分组
        Map<Integer, QuestionOption> existingOptionsMap = question.getOptions().stream()
                .collect(Collectors.toMap(QuestionOption::getId, Function.identity()));

        // 1. 删除：如果数据库中的选项在传入的DTO中不存在，则删除
        Set<QuestionOption> toDelete = new HashSet<>();
        for (QuestionOption existingOption : question.getOptions()) {
            if (!dtoMap.containsKey(existingOption.getId())) {
                toDelete.add(existingOption);
            }
        }
        question.getOptions().removeAll(toDelete);
        optionRepository.deleteAll(toDelete);

        // 2. 更新或新增
        for (QuestionOptionSaveDTO dto : optionDTOs) {
            if (dto.getId() != null && existingOptionsMap.containsKey(dto.getId())) {
                // 更新
                QuestionOption optionToUpdate = existingOptionsMap.get(dto.getId());
                adminMapper.updateOptionFromDto(dto, optionToUpdate);
            } else {
                // 新增
                QuestionOption newOption = adminMapper.dtoToOption(dto);
                newOption.setQuestion(question);
                question.getOptions().add(newOption);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionTendencyDTO> getTendenciesForOption(Integer optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new ResourceNotFoundException("选项不存在");
        }
        List<OptionPositionTendency> tendencies = tendencyRepository.findByOptionIdWithDetails(optionId);
        return adminMapper.toPositionTendencyDTOList(tendencies);
    }

    @Override
    @Transactional
    public List<PositionTendencyDTO> updateTendenciesForOption(Integer optionId, UpdateTendenciesRequestDTO request) {
        // 1. 验证选项是否存在
        QuestionOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ResourceNotFoundException("要更新的选项不存在"));

        // 2. (策略) 先删除该选项所有旧的倾向设置
        tendencyRepository.deleteByOptionId(optionId);

        // 3. 遍历请求中的新设置，创建并保存新的倾向实体
        if (request.getTendencies() != null && !request.getTendencies().isEmpty()) {
            List<OptionPositionTendency> newTendencies = new ArrayList<>();
            for (PositionTendencyDTO dto : request.getTendencies()) {
                OptionPositionTendency newTendency = new OptionPositionTendency();
                newTendency.setOption(option);
                // 使用 getReferenceById 避免一次额外的数据库查询
                newTendency.setPosition(positionRepository.getReferenceById(dto.getPositionId()));
                newTendency.setTendencyScore(dto.getTendencyScore());
                newTendencies.add(newTendency);
            }
            tendencyRepository.saveAll(newTendencies);
        }

        // 4. 返回更新后的最新配置
        return getTendenciesForOption(optionId);
    }
}
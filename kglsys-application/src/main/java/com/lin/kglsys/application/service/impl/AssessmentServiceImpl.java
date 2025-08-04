package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.AssessmentMapper;
import com.lin.kglsys.application.mapper.PositionMapper;
import com.lin.kglsys.application.service.AssessmentService;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.domain.entity.*;
import com.lin.kglsys.dto.request.AnswerDTO;
import com.lin.kglsys.dto.request.AssessmentSubmissionRequest;
import com.lin.kglsys.dto.response.QuestionDTO;
import com.lin.kglsys.dto.response.RecommendedPositionDTO;
import com.lin.kglsys.infra.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentQuestionRepository questionRepository;
    private final UserAssessmentAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final AssessmentMapper assessmentMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final OptionPositionTendencyRepository tendencyRepository;
    private final UserTargetPositionRepository userTargetPositionRepository;
    private final PositionMapper positionMapper;

    private static final String ASSESSMENT_SUBMIT_LOCK_PREFIX = "kglsys:assessment:submit_lock:";
    private static final long LOCK_DURATION_MINUTES = 5;
    private static final int RECOMMENDATION_COUNT = 3;

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getAssessmentQuestions() {
        List<AssessmentQuestion> questions = questionRepository.findByIsActiveWithDetails(true);
        return assessmentMapper.toQuestionDTOList(questions);
    }

    @Override
    @Transactional
    public List<RecommendedPositionDTO> submitAssessment(AssessmentSubmissionRequest request) {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) throw new UserNotFoundException();

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // 1. 查找现有答案并将其按问题ID进行映射以快速访问。
        List<UserAssessmentAnswer> existingAnswers = answerRepository.findByUserId(userId);
        Map<Integer, UserAssessmentAnswer> answerMap = existingAnswers.stream()
                .collect(Collectors.toMap(answer -> answer.getQuestion().getId(), answer -> answer));

        List<UserAssessmentAnswer> answersToSave = new ArrayList<>();

        // 2. 遍历请求的新回复
        for (AnswerDTO answerDTO : request.getAnswers()) {
            UserAssessmentAnswer answerToUpdate = answerMap.get(answerDTO.getQuestionId());

            if (answerToUpdate != null) {
                // 3a. 如果答案已经存在，更新它。
                QuestionOption newSelectedOption = new QuestionOption();
                newSelectedOption.setId(answerDTO.getSelectedOptionId());
                answerToUpdate.setSelectedOption(newSelectedOption);
                answerToUpdate.setCreatedAt(LocalDateTime.now()); // Atualizar o timestamp da resposta
                answersToSave.add(answerToUpdate);
            } else {
                // 3b. 如果不存在答案（边界情况），则创建一个新的。
                answersToSave.add(convertToAnswerEntity(user, answerDTO));
            }
        }

        // 4. 保存所有更新或新的实体
        answerRepository.saveAll(answersToSave);

        return calculateRecommendations(request.getAnswers());
    }

    private List<RecommendedPositionDTO> calculateRecommendations(List<AnswerDTO> answers) {
        // 1. 提取所有选择的选项ID
        List<Integer> selectedOptionIds = answers.stream()
                .map(AnswerDTO::getSelectedOptionId)
                .toList();

        // 2. 一次性查询所有相关的岗位倾向
        List<OptionPositionTendency> tendencies = tendencyRepository.findByOptionIdInWithDetails(selectedOptionIds);

        // 3. 使用Stream API聚合每个岗位的总分
        Map<Position, BigDecimal> positionScores = tendencies.stream()
                .collect(Collectors.groupingBy(
                        OptionPositionTendency::getPosition,
                        Collectors.reducing(BigDecimal.ZERO, OptionPositionTendency::getTendencyScore, BigDecimal::add)
                ));

        // 4. 排序并获取Top N的岗位
        List<Map.Entry<Position, BigDecimal>> topPositions = positionScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(RECOMMENDATION_COUNT)
                .toList();

        // 5. 转换为DTO返回给前端
        return topPositions.stream().map(entry -> {
            RecommendedPositionDTO dto = positionMapper.toRecommendedPositionDTO(entry.getKey());
            dto.setMatchScore(entry.getValue());
            return dto;
        }).collect(Collectors.toList());
    }

    private UserAssessmentAnswer convertToAnswerEntity(User user, AnswerDTO dto) {
        // 这里不使用JPA的getReferenceById，因为它返回的是代理对象，
        // 如果后续需要访问对象属性可能会触发懒加载异常。直接创建新对象并设置ID即可。
        AssessmentQuestion question = new AssessmentQuestion();
        question.setId(dto.getQuestionId());

        QuestionOption option = new QuestionOption();
        option.setId(dto.getSelectedOptionId());

        UserAssessmentAnswer answer = new UserAssessmentAnswer();
        answer.setUser(user);
        answer.setQuestion(question);
        answer.setSelectedOption(option);
        answer.setCreatedAt(LocalDateTime.now());
        return answer;
    }
}
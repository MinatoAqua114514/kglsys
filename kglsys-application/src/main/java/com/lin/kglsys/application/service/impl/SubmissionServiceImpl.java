package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.service.SubmissionService;
import com.lin.kglsys.common.constant.RedisConstants;
import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.exception.business.ProblemNotFoundException;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.common.exception.business.SubmissionNotFoundException;
import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.domain.entity.Problem;
import com.lin.kglsys.domain.entity.Submission;
import com.lin.kglsys.domain.entity.User;
import com.lin.kglsys.domain.valobj.SubmissionStatus;
import com.lin.kglsys.dto.event.JudgeResultReceivedEvent;
import com.lin.kglsys.dto.mq.JudgeResultDTO;
import com.lin.kglsys.dto.mq.JudgeTaskDTO;
import com.lin.kglsys.dto.mq.TestCaseDTO;
import com.lin.kglsys.dto.request.CodeSubmissionRequest;
import com.lin.kglsys.dto.response.CodeSubmissionResponse;
import com.lin.kglsys.infra.repository.ProblemRepository;
import com.lin.kglsys.infra.repository.SubmissionRepository;
import com.lin.kglsys.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final ProblemRepository problemRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${app.rabbitmq.tasks-queue}")
    private String tasksQueueName;

    @Override
    @Transactional
    public CodeSubmissionResponse submitCode(CodeSubmissionRequest request) {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) throw new UserNotFoundException();

        User user = userRepository.getReferenceById(userId);
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(ProblemNotFoundException::new);

        // 1. 创建并保存一个初始的 Submission 记录到数据库
        Submission submission = new Submission();
        submission.setUser(user);
        submission.setProblem(problem);
        submission.setSourceCode(request.getSourceCode());
        submission.setLanguage(String.valueOf(request.getLanguageId())); // 暂时存ID
        submission.setStatus(SubmissionStatus.PENDING);
        submission.setSubmittedAt(LocalDateTime.now());
        Submission savedSubmission = submissionRepository.save(submission);

        // 2. 在 Redis 中创建任务状态记录 (为了快速查询，避免频繁查库)
        String submissionIdStr = savedSubmission.getId().toString();
        String redisKey = RedisConstants.CODE_SUBMISSION_STATUS_KEY + submissionIdStr;
        redisTemplate.opsForHash().put(redisKey, "status", "PENDING");

        // 3. 构造发送到 RabbitMQ 的任务 DTO
        List<TestCaseDTO> testCaseDTOs = problem.getTestCases().stream()
                .map(tc -> new TestCaseDTO(tc.getInput(), tc.getExpectedOutput()))
                .collect(Collectors.toList());

        JudgeTaskDTO taskDTO = new JudgeTaskDTO(
                submissionIdStr,
                request.getSourceCode(),
                request.getLanguageId(),
                testCaseDTOs,
                problem.getTimeLimitMs() / 1000.0f, // 转为秒
                problem.getMemoryLimitKb()
        );

        // 4. 发送任务到消息队列
        rabbitTemplate.convertAndSend(tasksQueueName, taskDTO);
        log.info("Published judge task to RabbitMQ for submissionId: {}", submissionIdStr);

        // 5. 返回唯一的提交ID
        return new CodeSubmissionResponse(submissionIdStr);
    }

    /**
     * 内部的事件处理器。
     * 它监听 JudgeResultReceivedEvent 事件。
     * @param event 包含判题结果的事件对象
     */
    @EventListener
    @Transactional
    public void handleJudgeResultEvent(JudgeResultReceivedEvent event) {
        JudgeResultDTO resultDTO = event.getResultDTO();
        log.info("Handling JudgeResultReceivedEvent for submissionId: {}", resultDTO.getSubmissionId());

        Long submissionId = Long.parseLong(resultDTO.getSubmissionId());

        // 1. 从数据库查找并更新 Submission 记录
        Submission submission = submissionRepository.findByIdWithUserAndRoles(submissionId)
                .orElseThrow(() -> new SubmissionNotFoundException(ResultCode.SUBMISSION_NOT_FOUND));

        SubmissionStatus status;
        try {
            status = SubmissionStatus.valueOf(resultDTO.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Received unknown submission status '{}' for submissionId: {}. Defaulting to SYSTEM_ERROR.", resultDTO.getStatus(), submissionId);
            status = SubmissionStatus.SYSTEM_ERROR; // Default to a known error state
        }

        submission.setStatus(status);
        submission.setExecutionTimeMs(resultDTO.getExecutionTimeMs());
        submission.setMemoryUsedKb(resultDTO.getMemoryUsedKb());
        submission.setJudgeOutput(resultDTO.getJudgeOutput());
        submissionRepository.save(submission);

        // 2. 更新 Redis 中的状态
        String redisKey = RedisConstants.CODE_SUBMISSION_STATUS_KEY + resultDTO.getSubmissionId();
        Map<String, String> resultMap = Map.of(
                "status", resultDTO.getStatus(),
                "executionTimeMs", String.valueOf(resultDTO.getExecutionTimeMs()),
                "memoryUsedKb", String.valueOf(resultDTO.getMemoryUsedKb()),
                "judgeOutput", resultDTO.getJudgeOutput()
        );
        redisTemplate.opsForHash().putAll(redisKey, resultMap);
        log.info("Updated submission {} in DB and Redis with status: {}", submissionId, status);

        // 3. 通过 WebSocket 推送结果给指定用户
        User user = submission.getUser();
        String username = user.getEmail(); // The username is the user's email
        String destination = "/queue/submission-updates";

        // The first argument to convertAndSendToUser should be the username (principal name), not the user ID.
        messagingTemplate.convertAndSendToUser(username, destination, resultDTO);
        log.info("Sending WebSocket update for submission {} to user '{}' at destination '{}'", submissionId, username, destination);
    }
}
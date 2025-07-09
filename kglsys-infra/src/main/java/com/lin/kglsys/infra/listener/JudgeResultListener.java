package com.lin.kglsys.infra.listener;

import com.lin.kglsys.dto.event.JudgeResultReceivedEvent;
import com.lin.kglsys.dto.mq.JudgeResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher; // 导入事件发布器
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JudgeResultListener {

    // 注入 Spring 的事件发布器
    private final ApplicationEventPublisher eventPublisher;

    @RabbitListener(queues = "${app.rabbitmq.results-queue}")
    public void onJudgeResult(JudgeResultDTO resultDTO) {
        if (resultDTO == null || resultDTO.getSubmissionId() == null) {
            log.warn("Received an invalid judge result message: {}", resultDTO);
            return;
        }
        log.info("Received judge result for submissionId: {}. Publishing as an application event.", resultDTO.getSubmissionId());
        try {
            // 将收到的消息包装成事件并发布
            JudgeResultReceivedEvent event = new JudgeResultReceivedEvent(this, resultDTO);
            eventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error("Error publishing judge result event for submissionId: {}", resultDTO.getSubmissionId(), e);
        }
    }
}
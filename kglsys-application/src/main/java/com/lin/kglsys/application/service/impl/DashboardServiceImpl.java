package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.service.DashboardService;
import com.lin.kglsys.common.exception.business.UserNotFoundException;
import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.domain.entity.LearningPath;
import com.lin.kglsys.domain.entity.LearningPathNode;
import com.lin.kglsys.domain.entity.UserLearningProgress;
import com.lin.kglsys.domain.entity.UserLearningStatus;
import com.lin.kglsys.domain.valobj.SubmissionStatus;
import com.lin.kglsys.domain.valobj.UserLearningProgressStatus;
import com.lin.kglsys.dto.response.dashboard.DashboardSummaryDTO;
import com.lin.kglsys.dto.response.dashboard.NextStepDTO;
import com.lin.kglsys.infra.repository.LearningPathNodeRepository;
import com.lin.kglsys.infra.repository.SubmissionRepository;
import com.lin.kglsys.infra.repository.UserLearningProgressRepository;
import com.lin.kglsys.infra.repository.UserLearningStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserLearningStatusRepository userLearningStatusRepository;
    private final UserLearningProgressRepository progressRepository;
    private final SubmissionRepository submissionRepository;
    private final LearningPathNodeRepository nodeRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardSummaryDTO getDashboardSummary() {
        Long userId = UserContextHolder.getUserId();
        if (userId == null) throw new UserNotFoundException();

        DashboardSummaryDTO summary = new DashboardSummaryDTO();

        // 1. 获取已完成的编程题数
        long completedProblems = submissionRepository.countDistinctProblemsByUserIdAndStatus(userId, SubmissionStatus.ACCEPTED);
        summary.setCompletedProblems(completedProblems);

        // 2. 获取用户的学习状态和激活的学习路径
        UserLearningStatus learningStatus = userLearningStatusRepository.findByIdWithDetails(userId)
                .orElse(null);

        if (learningStatus == null || learningStatus.getActivePath() == null) {
            // 如果用户没有激活的学习路径，返回默认的摘要信息
            summary.setTargetPosition("暂未选择");
            summary.setNextStep(createDefaultNextStep());
            return summary;
        }

        LearningPath activePath = learningStatus.getActivePath();
        summary.setTargetPosition(activePath.getPosition().getDisplayName());

        // 3. 统计学习进度
        List<UserLearningProgress> progresses = progressRepository.findUserProgressForPath(userId, activePath.getId());
        long masteredNodes = progresses.stream().filter(p -> p.getStatus() == UserLearningProgressStatus.MASTERED).count();
        long inProgressNodes = progresses.stream().filter(p -> p.getStatus() == UserLearningProgressStatus.IN_PROGRESS).count();
        summary.setMasteredNodes(masteredNodes);
        summary.setInProgressNodes(inProgressNodes);

        // 4. 计算总进度
        long totalNodes = nodeRepository.countByPathId(activePath.getId());
        if (totalNodes > 0) {
            summary.setOverallProgress((int) ((double) masteredNodes / totalNodes * 100));
        }

        // 5. 查找下一步建议
        summary.setNextStep(findNextStep(userId, activePath));

        return summary;
    }

    private NextStepDTO findNextStep(Long userId, LearningPath activePath) {
        // 优先查找正在进行的节点
        List<UserLearningProgress> inProgressNodes = progressRepository.findFirstIncompleteProgress(userId, activePath.getId(), UserLearningProgressStatus.MASTERED);
        if (!inProgressNodes.isEmpty()) {
            LearningPathNode nextNode = inProgressNodes.get(0).getPathNode();
            return createNextStepDTO(nextNode, "继续学习");
        }

        // 如果没有正在进行的，就找第一个未开始的
        List<LearningPathNode> allNodes = nodeRepository.findByPathIdOrderBySequenceAsc(activePath.getId());
        if (!allNodes.isEmpty()) {
            return createNextStepDTO(allNodes.get(0), "开始学习");
        }

        return createDefaultNextStep();
    }

    private NextStepDTO createNextStepDTO(LearningPathNode node, String buttonText) {
        NextStepDTO nextStep = new NextStepDTO();
        nextStep.setTitle("学习 \"" + node.getNodeTitle() + "\"");
        nextStep.setDescription(node.getNodeDescription());
        nextStep.setButtonText(buttonText);
        // 假设前端路径是 /learning-path/{pathId}
        nextStep.setPath("/learning-path/" + node.getPath().getId());
        return nextStep;
    }

    private NextStepDTO createDefaultNextStep() {
        NextStepDTO nextStep = new NextStepDTO();
        nextStep.setTitle("选择一个目标岗位");
        nextStep.setDescription("完成职业测评，或手动选择一个感兴趣的岗位来开启你的学习之旅。");
        nextStep.setButtonText("前往测评");
        nextStep.setPath("/assessment"); // 指向测评页面
        return nextStep;
    }
}
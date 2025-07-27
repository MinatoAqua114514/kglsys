package com.lin.kglsys.dto.response.dashboard;

import lombok.Data;

@Data
public class DashboardSummaryDTO {
    private String targetPosition;
    private long masteredNodes;
    private long inProgressNodes;
    private long completedProblems;
    private int overallProgress; // 0-100的整数
    private NextStepDTO nextStep;
}
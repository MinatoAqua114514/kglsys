package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.response.dashboard.DashboardSummaryDTO;

public interface DashboardService {

    /**
     * 获取当前用户的仪表盘摘要信息
     * @return 仪表盘摘要DTO
     */
    DashboardSummaryDTO getDashboardSummary();
}
package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.DashboardService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.response.dashboard.DashboardSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * 获取仪表盘摘要信息
     * @return 包含摘要信息的统一响应体
     */
    @GetMapping("/summary")
    public ApiResult<DashboardSummaryDTO> getDashboardSummary() {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
        return ApiResult.success(summary);
    }
}
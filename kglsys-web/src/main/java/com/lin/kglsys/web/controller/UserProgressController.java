package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.UserProgressService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.UpdateProgressRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/learning-path-nodes")
@RequiredArgsConstructor
public class UserProgressController {

    private final UserProgressService userProgressService;

    /**
     * UR-011: 更新单个学习节点的进度
     * @param nodeId 节点ID
     * @param request 包含新状态的请求体
     * @return 统一成功响应
     */
    @PostMapping("/{nodeId}/progress")
    public ApiResult<Void> updateNodeProgress(
            @PathVariable Long nodeId,
            @Valid @RequestBody UpdateProgressRequest request) {
        userProgressService.updateNodeProgress(nodeId, request.getNewStatus());
        return ApiResult.success();
    }
}
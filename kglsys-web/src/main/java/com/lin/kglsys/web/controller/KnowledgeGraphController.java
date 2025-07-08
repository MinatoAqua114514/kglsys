package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.KnowledgeGraphService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.response.KgEntityDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/entities")
@RequiredArgsConstructor
public class KnowledgeGraphController {

    private final KnowledgeGraphService knowledgeGraphService;

    /**
     * UR-010: 获取知识实体的详细信息
     * @param entityId 知识实体ID
     * @return 包含实体详情、关联资源和题目的统一响应体
     */
    @GetMapping("/{entityId}")
    public ApiResult<KgEntityDetailDTO> getEntityDetails(@PathVariable Long entityId) {
        KgEntityDetailDTO entityDetails = knowledgeGraphService.getEntityDetails(entityId);
        return ApiResult.success(entityDetails);
    }
}
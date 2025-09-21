package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminEntityContentService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.EntityLinkRequestDTO;
import com.lin.kglsys.dto.response.LinkedContentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/entities/{entityId}/content")
@RequiredArgsConstructor
public class AdminEntityContentController {

    private final AdminEntityContentService contentService;

    @GetMapping
    public ApiResult<LinkedContentDTO> getLinkedContent(@PathVariable Long entityId) {
        return ApiResult.success(contentService.getLinkedContent(entityId));
    }

    @PutMapping
    public ApiResult<LinkedContentDTO> updateLinkedContent(
            @PathVariable Long entityId,
            @RequestBody EntityLinkRequestDTO dto) {
        return ApiResult.success(contentService.updateLinkedContent(entityId, dto));
    }
}
package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminLearningPathNodeService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.LearningPathNodeSaveDTO;
import com.lin.kglsys.dto.response.LearningPathNodeDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminLearningPathNodeController {

    private final AdminLearningPathNodeService nodeService;

    @GetMapping("/paths/{pathId}/nodes")
    public ApiResult<List<LearningPathNodeDTO>> listNodes(@PathVariable Long pathId) {
        return ApiResult.success(nodeService.listNodesForPath(pathId));
    }

    @PostMapping("/paths/{pathId}/nodes")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<LearningPathNodeDTO> createNode(@PathVariable Long pathId, @Valid @RequestBody LearningPathNodeSaveDTO dto) {
        return ApiResult.success(nodeService.createNode(pathId, dto));
    }

    @PutMapping("/nodes/{nodeId}")
    public ApiResult<LearningPathNodeDTO> updateNode(@PathVariable Long nodeId, @Valid @RequestBody LearningPathNodeSaveDTO dto) {
        return ApiResult.success(nodeService.updateNode(nodeId, dto));
    }

    @DeleteMapping("/nodes/{nodeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult<Void> deleteNode(@PathVariable Long nodeId) {
        nodeService.deleteNode(nodeId);
        return ApiResult.success();
    }
}
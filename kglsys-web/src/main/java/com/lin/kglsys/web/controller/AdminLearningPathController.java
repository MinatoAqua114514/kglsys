package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminLearningPathService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.LearningPathSaveDTO;
import com.lin.kglsys.dto.response.LearningPathDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminLearningPathController {

    private final AdminLearningPathService pathService;

    @GetMapping("/positions/{positionId}/paths")
    public ApiResult<List<LearningPathDTO>> listPathsForPosition(@PathVariable Integer positionId) {
        return ApiResult.success(pathService.listPathsForPosition(positionId));
    }

    @GetMapping("/paths/{pathId}")
    public ApiResult<LearningPathDTO> getPathById(@PathVariable Long pathId) {
        return ApiResult.success(pathService.getPathById(pathId));
    }

    @PostMapping("/positions/{positionId}/paths")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<LearningPathDTO> createPath(@PathVariable Integer positionId, @Valid @RequestBody LearningPathSaveDTO dto) {
        return ApiResult.success(pathService.createPath(positionId, dto));
    }

    @PutMapping("/paths/{pathId}")
    public ApiResult<LearningPathDTO> updatePath(@PathVariable Long pathId, @Valid @RequestBody LearningPathSaveDTO dto) {
        return ApiResult.success(pathService.updatePath(pathId, dto));
    }

    @DeleteMapping("/paths/{pathId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult<Void> deletePath(@PathVariable Long pathId) {
        pathService.deletePath(pathId);
        return ApiResult.success();
    }
}
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

    /**
     * 获取指定岗位的所有学习路径
     * @param positionId 岗位ID
     * @return 该岗位下的所有学习路径列表
     */
    @GetMapping("/positions/{positionId}/paths")
    public ApiResult<List<LearningPathDTO>> listPathsForPosition(@PathVariable Integer positionId) {
        return ApiResult.success(pathService.listPathsForPosition(positionId));
    }

    /**
     * 根据ID获取学习路径详情
     * @param pathId 学习路径ID
     * @return 学习路径详情
     */
    @GetMapping("/paths/{pathId}")
    public ApiResult<LearningPathDTO> getPathById(@PathVariable Long pathId) {
        return ApiResult.success(pathService.getPathById(pathId));
    }

    /**
     * 为指定岗位创建新的学习路径
     * @param positionId 岗位ID
     * @param dto 学习路径数据
     * @return 创建的学习路径详情
     */
    @PostMapping("/positions/{positionId}/paths")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<LearningPathDTO> createPath(@PathVariable Integer positionId, @Valid @RequestBody LearningPathSaveDTO dto) {
        return ApiResult.success(pathService.createPath(positionId, dto));
    }

    /**
     * 更新指定ID的学习路径
     * @param pathId 学习路径ID
     * @param dto 更新后的学习路径数据
     * @return 更新后的学习路径详情
     */
    @PutMapping("/paths/{pathId}")
    public ApiResult<LearningPathDTO> updatePath(@PathVariable Long pathId, @Valid @RequestBody LearningPathSaveDTO dto) {
        return ApiResult.success(pathService.updatePath(pathId, dto));
    }

    /**
     * 删除指定ID的学习路径
     * @param pathId 学习路径ID
     */
    @DeleteMapping("/paths/{pathId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult<Void> deletePath(@PathVariable Long pathId) {
        pathService.deletePath(pathId);
        return ApiResult.success();
    }
}
package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.PositionService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.SelectTargetPositionRequest;
import com.lin.kglsys.dto.response.PositionDTO;
import com.lin.kglsys.dto.response.RecommendedPositionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    /**
     * 获取为当前用户推荐的岗位列表
     * @return 推荐岗位列表
     */
    @GetMapping("/recommended")
    public ApiResult<List<RecommendedPositionDTO>> getRecommendedPositions() {
        List<RecommendedPositionDTO> positions = positionService.getRecommendedPositions();
        return ApiResult.success(positions);
    }

    /**
     * 获取单个岗位详情
     * @param id 岗位ID
     * @return 岗位详情
     */
    @GetMapping("/{id}")
    public ApiResult<PositionDTO> getPositionDetails(@PathVariable Integer id) {
        PositionDTO position = positionService.getPositionDetails(id);
        return ApiResult.success(position);
    }

    /**
     * 用户手动选择目标岗位
     * @param request 包含岗位ID的请求体
     * @return 统一成功响应
     */
    @PostMapping("/target")
    public ApiResult<Void> selectTargetPosition(@Valid @RequestBody SelectTargetPositionRequest request) {
        positionService.selectTargetPosition(request.getPositionId());
        return ApiResult.success();
    }
}
package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.response.PositionDTO;
import com.lin.kglsys.dto.response.RecommendedPositionDTO;

import java.util.List;

public interface PositionService {

    /**
     * 获取为当前用户推荐的岗位列表
     * @return 推荐岗位DTO列表
     */
    List<RecommendedPositionDTO> getRecommendedPositions();

    /**
     * 获取指定ID的岗位详情
     * @param id 岗位ID
     * @return 岗位详情DTO
     */
    PositionDTO getPositionDetails(Integer id);

    /**
     * 用户手动选择一个目标岗位
     * @param positionId 岗位ID
     */
    void selectTargetPosition(Integer positionId);
}
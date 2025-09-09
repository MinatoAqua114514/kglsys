package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.PositionSaveDTO;
import com.lin.kglsys.dto.response.PositionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminPositionService {

    /**
     * 分页并按关键字搜索岗位列表
     * @param pageable 分页参数
     * @param keyword 搜索关键字 (匹配name或displayName)
     * @return 分页的岗位DTO
     */
    Page<PositionDTO> listPositions(Pageable pageable, String keyword);

    /**
     * 根据ID获取岗位详情
     * @param id 岗位ID
     * @return 岗位详情DTO
     */
    PositionDTO getPositionById(Integer id);

    /**
     * 创建新岗位
     * @param dto 岗位创建请求DTO
     * @return 创建后的岗位详情DTO
     */
    PositionDTO createPosition(PositionSaveDTO dto);

    /**
     * 更新岗位信息
     * @param id 岗位ID
     * @param dto 岗位更新请求DTO
     * @return 更新后的岗位详情DTO
     */
    PositionDTO updatePosition(Integer id, PositionSaveDTO dto);

    /**
     * 删除岗位
     * @param id 岗位ID
     */
    void deletePosition(Integer id);
}
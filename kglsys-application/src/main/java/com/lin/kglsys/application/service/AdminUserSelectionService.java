package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.response.UserSelectionViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserSelectionService {

    /**
     * 分页并按条件搜索用户的岗位和路径选择情况
     * @param pageable 分页参数
     * @param userKeyword 用户关键字 (匹配邮箱或昵称)
     * @param positionKeyword 岗位关键字 (匹配岗位显示名称)
     * @return 分页的用户选择视图DTO
     */
    Page<UserSelectionViewDTO> listUserSelections(Pageable pageable, String userKeyword, String positionKeyword);
}
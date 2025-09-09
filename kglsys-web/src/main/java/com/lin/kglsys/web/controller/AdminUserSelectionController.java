package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminUserSelectionService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.response.UserSelectionViewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/user-selections")
@RequiredArgsConstructor
public class AdminUserSelectionController {

    private final AdminUserSelectionService userSelectionService;

    /**
     * 获取用户选择的目标岗位和学习路径列表
     * @param pageable 分页参数 (e.g., ?page=0&size=10&sort=updatedAt,desc)
     * @param userKeyword (可选) 按用户邮箱或昵称模糊查询
     * @param positionKeyword (可选) 按岗位显示名称模糊查询
     * @return 分页的用户选择信息
     */
    @GetMapping
    public ApiResult<Page<UserSelectionViewDTO>> getUserSelections(
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String userKeyword,
            @RequestParam(required = false) String positionKeyword) {
        Page<UserSelectionViewDTO> selectionPage = userSelectionService.listUserSelections(pageable, userKeyword, positionKeyword);
        return ApiResult.success(selectionPage);
    }
}
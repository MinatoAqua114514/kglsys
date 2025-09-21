package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminUserProgressService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.response.UserProgressDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users/{userId}/progress")
@RequiredArgsConstructor
public class AdminUserProgressController {

    private final AdminUserProgressService userProgressService;

    @GetMapping
    public ApiResult<UserProgressDetailDTO> getUserProgress(@PathVariable Long userId) {
        return ApiResult.success(userProgressService.getUserProgressDetail(userId));
    }
}
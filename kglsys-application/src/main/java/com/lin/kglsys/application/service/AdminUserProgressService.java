package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.response.UserProgressDetailDTO;

public interface AdminUserProgressService {
    UserProgressDetailDTO getUserProgressDetail(Long userId);
}
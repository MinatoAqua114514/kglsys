package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.response.SubmissionViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminSubmissionService {
    Page<SubmissionViewDTO> listSubmissions(Pageable pageable, String userEmail, String problemTitle);
}
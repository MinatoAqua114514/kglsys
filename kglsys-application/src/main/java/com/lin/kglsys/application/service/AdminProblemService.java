package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.ProblemSaveDTO;
import com.lin.kglsys.dto.response.ProblemDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminProblemService {
    Page<ProblemDetailDTO> listProblems(Pageable pageable, String title);
    ProblemDetailDTO getProblemById(Long id);
    ProblemDetailDTO createProblem(ProblemSaveDTO dto);
    ProblemDetailDTO updateProblem(Long id, ProblemSaveDTO dto);
    void deleteProblem(Long id);
}
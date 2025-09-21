package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminProblemService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.ProblemSaveDTO;
import com.lin.kglsys.dto.response.ProblemDetailDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/problems")
@RequiredArgsConstructor
public class AdminProblemController {

    private final AdminProblemService problemService;

    @GetMapping
    public ApiResult<Page<ProblemDetailDTO>> listProblems(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(required = false) String title) {
        return ApiResult.success(problemService.listProblems(pageable, title));
    }

    @GetMapping("/{id}")
    public ApiResult<ProblemDetailDTO> getProblem(@PathVariable Long id) {
        return ApiResult.success(problemService.getProblemById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<ProblemDetailDTO> createProblem(@Valid @RequestBody ProblemSaveDTO dto) {
        return ApiResult.success(problemService.createProblem(dto));
    }

    @PutMapping("/{id}")
    public ApiResult<ProblemDetailDTO> updateProblem(@PathVariable Long id, @Valid @RequestBody ProblemSaveDTO dto) {
        return ApiResult.success(problemService.updateProblem(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult<Void> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ApiResult.success();
    }
}
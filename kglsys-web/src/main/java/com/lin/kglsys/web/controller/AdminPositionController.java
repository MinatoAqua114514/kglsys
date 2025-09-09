package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminPositionService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.PositionSaveDTO;
import com.lin.kglsys.dto.response.PositionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/positions")
@RequiredArgsConstructor
public class AdminPositionController {

    private final AdminPositionService adminPositionService;

    @GetMapping
    public ApiResult<Page<PositionDTO>> listPositions(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(required = false) String keyword) {
        return ApiResult.success(adminPositionService.listPositions(pageable, keyword));
    }

    @GetMapping("/{id}")
    public ApiResult<PositionDTO> getPosition(@PathVariable Integer id) {
        return ApiResult.success(adminPositionService.getPositionById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<PositionDTO> createPosition(@Valid @RequestBody PositionSaveDTO dto) {
        return ApiResult.success(adminPositionService.createPosition(dto));
    }

    @PutMapping("/{id}")
    public ApiResult<PositionDTO> updatePosition(@PathVariable Integer id, @Valid @RequestBody PositionSaveDTO dto) {
        return ApiResult.success(adminPositionService.updatePosition(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult<Void> deletePosition(@PathVariable Integer id) {
        adminPositionService.deletePosition(id);
        return ApiResult.success();
    }
}
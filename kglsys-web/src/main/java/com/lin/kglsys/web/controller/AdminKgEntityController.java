package com.lin.kglsys.web.controller;

import com.lin.kglsys.application.service.AdminKgEntityService;
import com.lin.kglsys.common.response.ApiResult;
import com.lin.kglsys.dto.request.KgEntitySaveDTO;
import com.lin.kglsys.dto.response.KgEntityDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/entities")
@RequiredArgsConstructor
public class AdminKgEntityController {

    private final AdminKgEntityService entityService;

    @GetMapping
    public ApiResult<Page<KgEntityDTO>> listEntities(
            @PageableDefault(sort = "id") Pageable pageable,
            @RequestParam(required = false) String keyword) {
        return ApiResult.success(entityService.listEntities(pageable, keyword));
    }

    @GetMapping("/{id}")
    public ApiResult<KgEntityDTO> getEntity(@PathVariable Long id) {
        return ApiResult.success(entityService.getEntityById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<KgEntityDTO> createEntity(@Valid @RequestBody KgEntitySaveDTO dto) {
        return ApiResult.success(entityService.createEntity(dto));
    }

    @PutMapping("/{id}")
    public ApiResult<KgEntityDTO> updateEntity(@PathVariable Long id, @Valid @RequestBody KgEntitySaveDTO dto) {
        return ApiResult.success(entityService.updateEntity(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResult<Void> deleteEntity(@PathVariable Long id) {
        entityService.deleteEntity(id);
        return ApiResult.success();
    }
}
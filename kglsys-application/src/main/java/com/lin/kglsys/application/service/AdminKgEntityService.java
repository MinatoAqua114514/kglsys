package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.KgEntitySaveDTO;
import com.lin.kglsys.dto.response.KgEntityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminKgEntityService {
    Page<KgEntityDTO> listEntities(Pageable pageable, String keyword);
    KgEntityDTO getEntityById(Long id);
    KgEntityDTO createEntity(KgEntitySaveDTO dto);
    KgEntityDTO updateEntity(Long id, KgEntitySaveDTO dto);
    void deleteEntity(Long id);
}
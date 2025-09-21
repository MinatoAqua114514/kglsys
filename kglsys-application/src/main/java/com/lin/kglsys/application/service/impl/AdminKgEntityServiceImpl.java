package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.AdminKgEntityMapper;
import com.lin.kglsys.application.service.AdminKgEntityService;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.domain.entity.KgEntity;
import com.lin.kglsys.dto.request.KgEntitySaveDTO;
import com.lin.kglsys.dto.response.KgEntityDTO;
import com.lin.kglsys.infra.repository.KgEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminKgEntityServiceImpl implements AdminKgEntityService {

    private final KgEntityRepository entityRepository;
    private final AdminKgEntityMapper entityMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<KgEntityDTO> listEntities(Pageable pageable, String keyword) {
        // 简单实现，可后续通过Specification扩展为多字段搜索
        Page<KgEntity> entityPage = entityRepository.findAll(pageable);
        return entityPage.map(entityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public KgEntityDTO getEntityById(Long id) {
        return entityRepository.findById(id)
                .map(entityMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("知识实体不存在"));
    }

    @Override
    @Transactional
    public KgEntityDTO createEntity(KgEntitySaveDTO dto) {
        // 可在此添加对 entityIdStr 的唯一性校验
        KgEntity entity = entityMapper.fromSaveDto(dto);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entityMapper.toDto(entityRepository.save(entity));
    }

    @Override
    @Transactional
    public KgEntityDTO updateEntity(Long id, KgEntitySaveDTO dto) {
        KgEntity entity = entityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("要更新的知识实体不存在"));
        entityMapper.updateFromSaveDto(dto, entity);
        entity.setUpdatedAt(LocalDateTime.now());
        return entityMapper.toDto(entityRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteEntity(Long id) {
        if (!entityRepository.existsById(id)) {
            throw new ResourceNotFoundException("要删除的知识实体不存在");
        }
        // 可在此添加业务检查，如实体是否被节点引用
        entityRepository.deleteById(id);
    }
}
package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.KgEntity;
import com.lin.kglsys.dto.request.KgEntitySaveDTO;
import com.lin.kglsys.dto.response.KgEntityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AdminKgEntityMapper {

    AdminKgEntityMapper INSTANCE = Mappers.getMapper(AdminKgEntityMapper.class);

    KgEntityDTO toDto(KgEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "properties", ignore = true)
    KgEntity fromSaveDto(KgEntitySaveDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "properties", ignore = true)
    void updateFromSaveDto(KgEntitySaveDTO dto, @MappingTarget KgEntity entity);
}
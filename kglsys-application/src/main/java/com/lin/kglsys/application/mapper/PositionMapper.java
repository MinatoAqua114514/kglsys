package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.Position;
import com.lin.kglsys.dto.response.PositionDTO;
import com.lin.kglsys.dto.response.RecommendedPositionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    PositionMapper INSTANCE = Mappers.getMapper(PositionMapper.class);

    PositionDTO toPositionDTO(Position position);

    // [修正] 移除这个有歧义的方法，MapStruct会自动处理列表映射
    // List<PositionDTO> toPositionDTOList(List<Position> positions);

    @Mapping(target = "matchScore", ignore = true) // [修正] 忽略 'matchScore' 属性的映射
    RecommendedPositionDTO toRecommendedPositionDTO(Position position);
}
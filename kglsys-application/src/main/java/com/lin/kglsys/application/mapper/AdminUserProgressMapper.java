package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.UserLearningProgress;
import com.lin.kglsys.dto.response.UserProgressNodeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AdminUserProgressMapper {

    AdminUserProgressMapper INSTANCE = Mappers.getMapper(AdminUserProgressMapper.class);

    @Mapping(source = "pathNode.id", target = "nodeId")
    @Mapping(source = "pathNode.nodeTitle", target = "nodeTitle")
    @Mapping(source = "pathNode.sequence", target = "sequence")
    UserProgressNodeDTO toUserProgressNodeDTO(UserLearningProgress progress);
}
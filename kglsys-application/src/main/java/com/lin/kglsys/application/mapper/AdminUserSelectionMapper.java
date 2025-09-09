package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.UserLearningStatus;
import com.lin.kglsys.dto.response.UserSelectionViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AdminUserSelectionMapper {

    AdminUserSelectionMapper INSTANCE = Mappers.getMapper(AdminUserSelectionMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "user.userProfile.nickname", target = "userNickname")
    @Mapping(source = "activePath.position.id", target = "positionId")
    @Mapping(source = "activePath.position.displayName", target = "positionName")
    @Mapping(source = "activePath.id", target = "learningPathId")
    @Mapping(source = "activePath.name", target = "learningPathName")
    @Mapping(source = "updatedAt", target = "updatedAt")
    UserSelectionViewDTO toUserSelectionViewDTO(UserLearningStatus status);
}
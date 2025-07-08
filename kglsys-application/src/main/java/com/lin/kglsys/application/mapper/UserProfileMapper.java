package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.User;
import com.lin.kglsys.domain.entity.UserProfile;
import com.lin.kglsys.dto.request.UpdateUserProfileRequest;
import com.lin.kglsys.dto.response.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    /**
     * 将 UserProfile 和 User 实体映射到 UserProfileResponse DTO。
     * @param profile 用户资料实体
     * @param user 用户实体
     * @return UserProfileResponse DTO
     */
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "profile.nickname", target = "nickname")
    @Mapping(source = "profile.avatarUrl", target = "avatarUrl")
    @Mapping(source = "profile.school", target = "school")
    @Mapping(source = "profile.major", target = "major")
    @Mapping(source = "profile.grade", target = "grade")
    UserProfileResponse toResponse(UserProfile profile, User user);

    /**
     * 从 UpdateUserProfileRequest DTO 更新 UserProfile 实体。
     * @param request DTO
     * @param profile 待更新的实体
     */
    void updateEntityFromDto(UpdateUserProfileRequest request, @MappingTarget UserProfile profile);
}
package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.Role;
import com.lin.kglsys.domain.entity.User;
import com.lin.kglsys.domain.entity.UserAssessmentAnswer;
import com.lin.kglsys.dto.response.AdminUserAnswerViewDTO;
import com.lin.kglsys.dto.response.AdminUserViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);

    @Mapping(source = "userProfile.nickname", target = "nickname")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToRoleNames")
    AdminUserViewDTO toAdminUserViewDTO(User user);

    @Named("rolesToRoleNames")
    default Set<String> rolesToRoleNames(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "user.userProfile.nickname", target = "userNickname")
    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "question.questionText", target = "questionText")
    @Mapping(source = "selectedOption.id", target = "selectedOptionId")
    @Mapping(source = "selectedOption.optionText", target = "selectedOptionText")
    AdminUserAnswerViewDTO toAdminUserAnswerViewDTO(UserAssessmentAnswer answer);
}
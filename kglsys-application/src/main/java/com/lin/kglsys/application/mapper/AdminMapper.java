package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.Role;
import com.lin.kglsys.domain.entity.User;
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
}
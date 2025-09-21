package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.Submission;
import com.lin.kglsys.dto.response.SubmissionViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AdminSubmissionMapper {

    AdminSubmissionMapper INSTANCE = Mappers.getMapper(AdminSubmissionMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "problem.id", target = "problemId")
    @Mapping(source = "problem.title", target = "problemTitle")
    SubmissionViewDTO toSubmissionViewDTO(Submission submission);
}
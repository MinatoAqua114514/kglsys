package com.lin.kglsys.application.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.kglsys.domain.entity.Problem;
import com.lin.kglsys.domain.entity.TestCase;
import com.lin.kglsys.dto.request.ProblemSaveDTO;
import com.lin.kglsys.dto.request.TestCaseSaveDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = ObjectMapper.class)
public interface AdminProblemMapper {

    AdminProblemMapper INSTANCE = Mappers.getMapper(AdminProblemMapper.class);

    // --- DTO to Entity ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "testCases", ignore = true) // 测试用例单独处理
    @Mapping(source = "tags", target = "tags", qualifiedByName = "listToString")
    Problem fromSaveDTO(ProblemSaveDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "problem", ignore = true) // 在服务层设置关联
    TestCase testCaseFromSaveDTO(TestCaseSaveDTO dto);

    // --- Update Entity from DTO ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "testCases", ignore = true)
    @Mapping(source = "tags", target = "tags", qualifiedByName = "listToString")
    void updateFromSaveDTO(ProblemSaveDTO dto, @MappingTarget Problem problem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "problem", ignore = true)
    void updateTestCaseFromSaveDTO(TestCaseSaveDTO dto, @MappingTarget TestCase testCase);

    // --- Helper Methods for JSON conversion ---
    @Named("listToString")
    default String listToString(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}
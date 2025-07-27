package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.Problem;
import com.lin.kglsys.domain.entity.TestCase;
import com.lin.kglsys.dto.response.ProblemDetailDTO;
import com.lin.kglsys.dto.response.TestCaseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProblemMapper {

    ProblemMapper INSTANCE = Mappers.getMapper(ProblemMapper.class);

    @Mapping(source = "difficulty", target = "difficulty")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "stringToList")
    @Mapping(target = "sampleTestCases", ignore = true) // 在服务层手动设置
    ProblemDetailDTO toProblemDetailDTO(Problem problem);

    TestCaseDTO toTestCaseDTO(TestCase testCase);

    @Named("stringToList")
    default List<String> stringToList(String tagsJson) {
        if (tagsJson == null || tagsJson.trim().isEmpty() || "null".equalsIgnoreCase(tagsJson.trim())) {
            return Collections.emptyList();
        }
        // 假设 tags 是一个 JSON 数组字符串, e.g., ["DP", "Array"]
        try {
            // 简单的解析，生产环境建议使用JSON库
            return Arrays.stream(tagsJson.replace("[", "").replace("]", "").replace("\"", "").split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // 如果解析失败，可以返回一个包含原始字符串的列表或空列表
            return Collections.singletonList(tagsJson);
        }
    }
}
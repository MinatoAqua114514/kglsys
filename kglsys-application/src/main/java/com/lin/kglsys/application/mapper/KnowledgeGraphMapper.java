package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.KgEntity;
import com.lin.kglsys.domain.entity.LearningResource;
import com.lin.kglsys.domain.entity.Problem;
import com.lin.kglsys.dto.response.KgEntityDetailDTO;
import com.lin.kglsys.dto.response.LearningResourceDTO;
import com.lin.kglsys.dto.response.ProblemBriefDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface KnowledgeGraphMapper {

    KnowledgeGraphMapper INSTANCE = Mappers.getMapper(KnowledgeGraphMapper.class);

    // --- Entity Detail Mappings ---

    @Mapping(target = "learningResources", ignore = true)
    @Mapping(target = "problems", ignore = true)
    KgEntityDetailDTO toKgEntityDetailDTO(KgEntity entity);

    @Mapping(source = "resourceType", target = "resourceType")
    @Mapping(source = "difficultyLevel", target = "difficultyLevel")
    LearningResourceDTO toLearningResourceDTO(LearningResource resource);

    @Mapping(source = "difficulty", target = "difficulty")
    ProblemBriefDTO toProblemBriefDTO(Problem problem);
}
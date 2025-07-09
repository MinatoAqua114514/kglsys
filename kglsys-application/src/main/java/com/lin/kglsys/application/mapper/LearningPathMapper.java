package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.KgEntity;
import com.lin.kglsys.domain.entity.KgRelationship;
import com.lin.kglsys.domain.entity.LearningPath;
import com.lin.kglsys.domain.entity.LearningPathNode;
import com.lin.kglsys.dto.response.LearningPathDTO;
import com.lin.kglsys.dto.response.LearningPathNodeDTO;
import com.lin.kglsys.dto.response.graph.KgLinkDTO;
import com.lin.kglsys.dto.response.graph.KgNodeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LearningPathMapper {

    LearningPathMapper INSTANCE = Mappers.getMapper(LearningPathMapper.class);

    // --- Learning Path Mappings ---
    @Mapping(source = "difficultyLevel", target = "difficultyLevel")
    @Mapping(target = "nodes", ignore = true) // [修正] 忽略 'nodes' 属性的映射
    LearningPathDTO toLearningPathDTO(LearningPath learningPath);

    // 明确忽略 'status' 属性，因为它由服务层手动填充
    @Mapping(target = "status", ignore = true)
    LearningPathNodeDTO toLearningPathNodeDTO(LearningPathNode node);

    List<LearningPathNodeDTO> toLearningPathNodeDTOList(List<LearningPathNode> nodes);

    // --- Knowledge Graph Mappings ---
    @Mapping(source = "id", target = "id")
    KgNodeDTO toKgNodeDTO(KgEntity entity);

    List<KgNodeDTO> toKgNodeDTOList(List<KgEntity> entities);

    @Mapping(source = "sourceEntity.id", target = "source")
    @Mapping(source = "targetEntity.id", target = "target")
    KgLinkDTO toKgLinkDTO(KgRelationship relationship);

    List<KgLinkDTO> toKgLinkDTOList(List<KgRelationship> relationships);
}
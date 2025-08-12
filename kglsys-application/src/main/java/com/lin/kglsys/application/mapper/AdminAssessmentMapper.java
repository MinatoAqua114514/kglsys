package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.AssessmentQuestion;
import com.lin.kglsys.domain.entity.OptionPositionTendency;
import com.lin.kglsys.domain.entity.QuestionOption;
import com.lin.kglsys.dto.request.AssessmentQuestionSaveDTO;
import com.lin.kglsys.dto.request.QuestionOptionSaveDTO;
import com.lin.kglsys.dto.response.PositionTendencyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminAssessmentMapper {

    AdminAssessmentMapper INSTANCE = Mappers.getMapper(AdminAssessmentMapper.class);

    // --- DTO to Entity ---

    @Mapping(target = "id", ignore = true) // ID由数据库生成
    @Mapping(target = "options", ignore = true) // 选项需要单独处理
    @Mapping(target = "createdAt", ignore = true)
    AssessmentQuestion dtoToQuestion(AssessmentQuestionSaveDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true) // 关联关系在服务层设置
    QuestionOption dtoToOption(QuestionOptionSaveDTO dto);

    // --- Update Entity from DTO ---

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateQuestionFromDto(AssessmentQuestionSaveDTO dto, @MappingTarget AssessmentQuestion question);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    void updateOptionFromDto(QuestionOptionSaveDTO dto, @MappingTarget QuestionOption option);

    @Mapping(source = "position.id", target = "positionId")
    @Mapping(source = "position.displayName", target = "positionName")
    PositionTendencyDTO toPositionTendencyDTO(OptionPositionTendency tendency);

    List<PositionTendencyDTO> toPositionTendencyDTOList(List<OptionPositionTendency> tendencies);
}
package com.lin.kglsys.application.mapper;

import com.lin.kglsys.domain.entity.AssessmentQuestion;
import com.lin.kglsys.domain.entity.QuestionOption;
import com.lin.kglsys.dto.response.OptionDTO;
import com.lin.kglsys.dto.response.QuestionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssessmentMapper {

    AssessmentMapper INSTANCE = Mappers.getMapper(AssessmentMapper.class);

    // MapStruct会自动处理List<QuestionOption>到List<OptionDTO>的映射
    QuestionDTO toQuestionDTO(AssessmentQuestion question);

    List<QuestionDTO> toQuestionDTOList(List<AssessmentQuestion> questions);

    OptionDTO toOptionDTO(QuestionOption option);
}
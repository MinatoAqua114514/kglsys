package com.lin.kglsys.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class QuestionDTO {
    private Integer id;
    private String questionText;
    private Integer sequence;
    private List<OptionDTO> options;
}
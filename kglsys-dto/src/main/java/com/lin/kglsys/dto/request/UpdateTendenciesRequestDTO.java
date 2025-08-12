package com.lin.kglsys.dto.request;

import com.lin.kglsys.dto.response.PositionTendencyDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class UpdateTendenciesRequestDTO {
    @Valid // 级联校验列表中的每个元素
    private List<PositionTendencyDTO> tendencies;
}
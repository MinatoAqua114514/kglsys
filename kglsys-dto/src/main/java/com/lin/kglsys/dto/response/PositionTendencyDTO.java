package com.lin.kglsys.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionTendencyDTO {
    @NotNull
    private Integer positionId;
    
    private String positionName; // 冗余字段，方便前端展示

    @NotNull
    private BigDecimal tendencyScore;
}
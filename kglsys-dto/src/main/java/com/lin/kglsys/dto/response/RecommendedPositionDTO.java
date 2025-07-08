package com.lin.kglsys.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true) // Inherits fields from PositionDTO
public class RecommendedPositionDTO extends PositionDTO {
    private BigDecimal matchScore;
}
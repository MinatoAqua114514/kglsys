package com.lin.kglsys.dto.response;

import lombok.Data;

@Data
public class PositionDTO {
    private Integer id;
    private String name;
    private String displayName;
    private String description;
    private String responsibilities;
    private String skillRequirements;
}
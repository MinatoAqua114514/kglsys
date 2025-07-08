package com.lin.kglsys.dto.response.graph;

import lombok.Data;

@Data
public class KgLinkDTO {
    private Long source; // source KgEntity ID
    private Long target; // target KgEntity ID
    private String relationType;
    private String description;
}
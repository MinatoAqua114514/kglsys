package com.lin.kglsys.dto.response.graph;

import lombok.Data;

@Data
public class KgNodeDTO {
    private Long id; // 使用KgEntity的ID
    private String name;
    private String type;
    private String description;
}
package com.lin.kglsys.dto.response.graph;

import lombok.Data;
import java.util.List;

@Data
public class KnowledgeGraphDTO {
    private List<KgNodeDTO> nodes;
    private List<KgLinkDTO> links;
}
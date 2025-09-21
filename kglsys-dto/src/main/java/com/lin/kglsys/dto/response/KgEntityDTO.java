package com.lin.kglsys.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class KgEntityDTO {
    private Long id;
    private String entityIdStr;
    private String name;
    private String type;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
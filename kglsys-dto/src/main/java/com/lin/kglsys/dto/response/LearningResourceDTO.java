package com.lin.kglsys.dto.response;

import lombok.Data;

@Data
public class LearningResourceDTO {
    private Long id;
    private String title;
    private String description;
    private String resourceType; // e.g., ARTICLE, VIDEO
    private String contentUrl;
    private String difficultyLevel; // e.g., BEGINNER, INTERMEDIATE
}
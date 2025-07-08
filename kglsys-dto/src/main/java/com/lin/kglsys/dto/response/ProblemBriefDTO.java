package com.lin.kglsys.dto.response;

import lombok.Data;

@Data
public class ProblemBriefDTO {
    private Long id;
    private String title;
    private String difficulty; // e.g., EASY, MEDIUM, HARD
}
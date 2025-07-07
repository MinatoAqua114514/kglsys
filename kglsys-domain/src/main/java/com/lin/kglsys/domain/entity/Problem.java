package com.lin.kglsys.domain.entity;

import com.lin.kglsys.domain.valobj.ProblemDifficulty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProblemDifficulty difficulty;

    @Column(name = "time_limit_ms", nullable = false)
    private Integer timeLimitMs;

    @Column(name = "memory_limit_kb", nullable = false)
    private Integer memoryLimitKb;

    @Column(columnDefinition = "JSON")
    private String tags;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TestCase> testCases;
}
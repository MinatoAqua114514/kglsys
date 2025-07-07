package com.lin.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "learning_path_nodes")
public class LearningPathNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_id", nullable = false)
    private LearningPath path;

    @Column(name = "node_title", nullable = false)
    private String nodeTitle;

    @Column(name = "node_description", columnDefinition = "TEXT")
    private String nodeDescription;

    @Column(nullable = false)
    private Integer sequence;

    @Column(name = "estimated_hours")
    private Integer estimatedHours;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 一个学习路径节点包含多个“节点-实体”的关联记录
    @OneToMany(mappedBy = "pathNode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PathNodeEntity> pathNodeEntities;

    // 一个节点可以作为多个依赖关系的前提
    @OneToMany(mappedBy = "prerequisiteNode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PathNodeDependency> prerequisiteFor;

    // 一个节点可以依赖于多个前提节点
    @OneToMany(mappedBy = "dependentNode", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PathNodeDependency> dependencies;
}
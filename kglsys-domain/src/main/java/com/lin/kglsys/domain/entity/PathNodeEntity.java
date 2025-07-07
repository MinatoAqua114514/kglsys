package com.lin.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "path_node_entities", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"path_node_id", "entity_id"})
})
public class PathNodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "path_node_id", nullable = false)
    private LearningPathNode pathNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    private KgEntity entity;

    @Column(name = "is_core")
    private boolean isCore = true;

    @Column(name = "learning_objective", columnDefinition = "TEXT")
    private String learningObjective;
}
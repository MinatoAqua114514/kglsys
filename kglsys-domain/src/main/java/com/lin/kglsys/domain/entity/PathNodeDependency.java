package com.lin.kglsys.domain.entity;

import com.lin.kglsys.domain.valobj.PathNodeDependencyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "path_node_dependencies", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"prerequisite_node_id", "dependent_node_id"})
})
public class PathNodeDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prerequisite_node_id", nullable = false)
    private LearningPathNode prerequisiteNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dependent_node_id", nullable = false)
    private LearningPathNode dependentNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "dependency_type", nullable = false)
    private PathNodeDependencyType dependencyType;
}
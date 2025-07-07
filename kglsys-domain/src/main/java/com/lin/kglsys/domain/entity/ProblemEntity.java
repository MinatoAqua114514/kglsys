package com.lin.kglsys.domain.entity;

import com.lin.kglsys.domain.valobj.TestDepth;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "problem_entities", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"problem_id", "entity_id"})
})
public class ProblemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    private KgEntity entity;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_depth", nullable = false)
    private TestDepth testDepth;
}
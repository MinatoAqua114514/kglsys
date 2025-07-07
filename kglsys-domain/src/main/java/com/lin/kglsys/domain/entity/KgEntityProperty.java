package com.lin.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kg_entity_properties")
public class KgEntityProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false)
    private KgEntity entity;

    @Column(name = "property_name", nullable = false, length = 100)
    private String propertyName;

    @Column(name = "property_value", columnDefinition = "TEXT", nullable = false)
    private String propertyValue;
}
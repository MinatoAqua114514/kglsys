package com.lin.kglsys.domain.entity;

import com.lin.kglsys.domain.valobj.UserTargetSource;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_target_positions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "position_id"})
})
public class UserTargetPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @Column(name = "match_score", precision = 5, scale = 2) // Nullable
    private BigDecimal matchScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserTargetSource source;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
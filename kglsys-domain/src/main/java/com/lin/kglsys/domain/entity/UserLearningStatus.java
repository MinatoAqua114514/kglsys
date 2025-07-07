package com.lin.kglsys.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_learning_status")
public class UserLearningStatus {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "active_path_id") // Nullable
    private LearningPath activePath;

    @Column(name = "assessment_completed_at") // Nullable
    private LocalDateTime assessmentCompletedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
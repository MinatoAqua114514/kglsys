package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.LearningResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for LearningResource entity.
 */
@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, Long> {
    // JpaRepository provides standard CRUD operations.
    // Custom query methods can be added here if needed in the future.
}
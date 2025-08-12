package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for QuestionOption entity.
 * Provides CRUD operations for managing question options.
 */
@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Integer> {
    // JpaRepository already provides all necessary methods like saveAll, deleteAll, etc.
    // No custom methods are needed for the current requirement.
}
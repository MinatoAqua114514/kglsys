package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for TestCase entity.
 */
@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    // JpaRepository provides standard CRUD operations.
    // Custom query methods can be added here if needed in the future.
}
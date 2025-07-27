package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    /**
     * 根据ID查找题目，并立即加载其所有测试用例以避免N+1问题。
     * @param problemId 题目ID
     * @return 包含测试用例的Problem实体
     */
    @Query("SELECT p FROM Problem p LEFT JOIN FETCH p.testCases WHERE p.id = :problemId")
    Optional<Problem> findByIdWithTestCases(Long problemId);
}
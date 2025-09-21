package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.Submission;
import com.lin.kglsys.domain.valobj.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long>, JpaSpecificationExecutor<Submission> {
    /**
     * 高效统计指定用户已通过（ACCEPTED）的题目总数。
     * 使用 distinct problem.id 来确保同一道题通过多次只被计算一次。
     * @param userId 用户ID
     * @param status 提交状态
     * @return 已通过的题目数量
     */
    @Query("SELECT COUNT(DISTINCT s.problem.id) FROM Submission s WHERE s.user.id = :userId AND s.status = :status")
    long countDistinctProblemsByUserIdAndStatus(Long userId, SubmissionStatus status);

    @Query("SELECT s FROM Submission s JOIN FETCH s.user u JOIN FETCH u.roles WHERE s.id = :submissionId")
    Optional<Submission> findByIdWithUserAndRoles(Long submissionId);
}
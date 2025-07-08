package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.AssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Integer> {

    /**
     * 查找所有激活的问卷问题，并立即加载其选项以避免N+1问题。
     * @param isActive 激活状态
     * @return 问题列表
     */
    @Query("SELECT q FROM AssessmentQuestion q JOIN FETCH q.options WHERE q.isActive = :isActive ORDER BY q.sequence")
    List<AssessmentQuestion> findByIsActiveWithDetails(boolean isActive);
}
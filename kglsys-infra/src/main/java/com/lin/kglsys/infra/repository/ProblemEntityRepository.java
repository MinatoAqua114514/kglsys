package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemEntityRepository extends JpaRepository<ProblemEntity, Long> {

    /**
     * 根据实体ID查找所有关联的编程题目
     * @param entityId 知识实体ID
     * @return 关联的编程题目列表
     */
    @Query("SELECT pe FROM ProblemEntity pe JOIN FETCH pe.problem WHERE pe.entity.id = :entityId")
    List<ProblemEntity> findByEntityIdWithProblems(Long entityId);

    @Modifying
    @Query("DELETE FROM ProblemEntity pe WHERE pe.entity.id = :entityId")
    void deleteByEntityId(Long entityId);

    boolean existsByProblemId(Long problemId);
}
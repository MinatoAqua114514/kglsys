package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.KgRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KgRelationshipRepository extends JpaRepository<KgRelationship, Long> {

    /**
     * 查找给定实体ID集合内部的所有关系
     * @param entityIds 实体ID列表
     * @return 关系列表
     */
    List<KgRelationship> findBySourceEntityIdInAndTargetEntityIdIn(List<Long> entityIds, List<Long> entityIds2);
}
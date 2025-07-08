package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.PathNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PathNodeEntityRepository extends JpaRepository<PathNodeEntity, Long> {

    /**
     * 根据学习路径节点ID，查找所有关联的知识实体
     * @param pathNodeId 学习路径节点ID
     * @return 路径节点-实体关联列表
     */
    @Query("SELECT pne FROM PathNodeEntity pne JOIN FETCH pne.entity WHERE pne.pathNode.id = :pathNodeId")
    List<PathNodeEntity> findByPathNodeIdWithEntities(Long pathNodeId);
}
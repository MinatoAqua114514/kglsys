package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.EntityLearningResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityLearningResourceRepository extends JpaRepository<EntityLearningResource, Long> {

    /**
     * 根据实体ID查找所有关联的学习资源
     * @param entityId 知识实体ID
     * @return 关联的学习资源列表
     */
    @Query("SELECT elr FROM EntityLearningResource elr JOIN FETCH elr.resource WHERE elr.entity.id = :entityId")
    List<EntityLearningResource> findByEntityIdWithResources(Long entityId);

    @Modifying // 声明这是一个修改操作
    @Query("DELETE FROM EntityLearningResource elr WHERE elr.entity.id = :entityId")
    void deleteByEntityId(Long entityId);
}
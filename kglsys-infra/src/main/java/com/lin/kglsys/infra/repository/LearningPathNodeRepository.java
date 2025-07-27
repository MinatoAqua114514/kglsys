package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.LearningPathNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathNodeRepository extends JpaRepository<LearningPathNode, Long> {

    /**
     * 根据路径ID查找所有节点，并按顺序排序
     * @param pathId 路径ID
     * @return 节点列表
     */
    List<LearningPathNode> findByPathIdOrderBySequenceAsc(Long pathId);

    /**
     * 统计指定路径下的节点总数
     * @param pathId 路径ID
     * @return 节点总数
     */
    long countByPathId(Long pathId);
}
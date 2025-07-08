package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Long> {

    /**
     * 根据岗位ID查找其默认的学习路径
     * @param positionId 岗位ID
     * @param isDefault 是否为默认
     * @return 学习路径
     */
    Optional<LearningPath> findByPositionIdAndIsDefault(Integer positionId, boolean isDefault);
}
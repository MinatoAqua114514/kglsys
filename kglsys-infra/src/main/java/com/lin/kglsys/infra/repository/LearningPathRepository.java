package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * 根据岗位ID查找其所有的学习路径
     * @param positionId 岗位ID
     * @return 学习路径列表
     */
    List<LearningPath> findByPositionId(Integer positionId);

    /**
     * 根据岗位ID和路径名称查找，用于唯一性校验
     * @param positionId 岗位ID
     * @param name 路径名称
     * @return Optional<LearningPath>
     */
    Optional<LearningPath> findByPositionIdAndName(Integer positionId, String name);

    /**
     * 检查是否有用户的激活路径是此路径
     * @param pathId 路径ID
     * @return 存在则返回true
     */
    @Query("SELECT CASE WHEN COUNT(uls) > 0 THEN true ELSE false END FROM UserLearningStatus uls WHERE uls.activePath.id = :pathId")
    boolean isUsedAsActivePath(Long pathId);
}
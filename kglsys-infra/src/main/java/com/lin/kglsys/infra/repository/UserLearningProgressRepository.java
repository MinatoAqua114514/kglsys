package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.UserLearningProgress;
import com.lin.kglsys.domain.valobj.UserLearningProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLearningProgressRepository extends JpaRepository<UserLearningProgress, Long> {

    /**
     * 根据用户ID和节点ID查找学习进度记录
     * @param userId 用户ID
     * @param pathNodeId 学习路径节点ID
     * @return 学习进度记录
     */
    Optional<UserLearningProgress> findByUserIdAndPathNodeId(Long userId, Long pathNodeId);

    /**
     * 根据用户ID和学习路径ID，一次性获取该路径下所有节点的学习进度
     * @param userId 用户ID
     * @param pathId 学习路径ID
     * @return 学习进度列表
     */
    @Query("SELECT p FROM UserLearningProgress p WHERE p.user.id = :userId AND p.pathNode.path.id = :pathId")
    List<UserLearningProgress> findUserProgressForPath(Long userId, Long pathId);

    /**
     * 查找指定用户在特定学习路径下，第一个状态不是 MASTERED 的节点。
     * 按 sequence 排序来确保是“下一个”节点。
     * @param userId 用户ID
     * @param pathId 学习路径ID
     * @param status 要排除的状态
     * @return 第一个未完成的节点进度记录
     */
    @Query("SELECT p FROM UserLearningProgress p JOIN FETCH p.pathNode pn " +
            "WHERE p.user.id = :userId AND pn.path.id = :pathId AND p.status <> :status " +
            "ORDER BY pn.sequence ASC")
    List<UserLearningProgress> findFirstIncompleteProgress(Long userId, Long pathId, UserLearningProgressStatus status);
}
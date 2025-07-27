package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.UserLearningStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLearningStatusRepository extends JpaRepository<UserLearningStatus, Long> {

    /**
     * 根据用户ID查找其学习状态，并立即加载关联的激活路径及其岗位信息。
     * @param userId 用户ID
     * @return 包含详细信息的用户学习状态
     */
    @Query("SELECT uls FROM UserLearningStatus uls LEFT JOIN FETCH uls.activePath ap LEFT JOIN FETCH ap.position WHERE uls.id = :userId")
    Optional<UserLearningStatus> findByIdWithDetails(Long userId);
}
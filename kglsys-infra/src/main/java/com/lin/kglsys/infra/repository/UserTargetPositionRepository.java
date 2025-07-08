package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.UserTargetPosition;
import com.lin.kglsys.domain.valobj.UserTargetSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTargetPositionRepository extends JpaRepository<UserTargetPosition, Long> {

    /**
     * 根据用户ID查找其所有目标岗位
     * @param userId 用户ID
     * @return 目标岗位列表
     */
    List<UserTargetPosition> findByUserId(Long userId);

    /**
     * 根据用户ID和来源删除目标岗位记录。
     * 用于在重新计算推荐时，清空旧的推荐结果。
     * @param userId 用户ID
     * @param source 来源 (ASSESSMENT 或 MANUAL)
     */
    void deleteByUserIdAndSource(Long userId, UserTargetSource source);

    /**
     * 根据用户ID和岗位ID查找记录，用于检查是否已存在
     */
    Optional<UserTargetPosition> findByUserIdAndPositionId(Long userId, Integer positionId);
}
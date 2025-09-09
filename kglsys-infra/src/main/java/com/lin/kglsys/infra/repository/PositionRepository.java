package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer>, JpaSpecificationExecutor<Position> {

    /**
     * 根据内部名称或显示名称查找岗位，用于唯一性校验
     * @param name 内部名称
     * @param displayName 显示名称
     * @return Optional<Position>
     */
    Optional<Position> findByNameOrDisplayName(String name, String displayName);
}
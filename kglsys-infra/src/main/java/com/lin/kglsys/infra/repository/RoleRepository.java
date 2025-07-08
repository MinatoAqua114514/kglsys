package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * 根据角色名称查找角色
     * @param name 角色名称 (e.g., "STUDENT", "ADMIN")
     * @return Optional<Role>
     */
    Optional<Role> findByName(String name);
}
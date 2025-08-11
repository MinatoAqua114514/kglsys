package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据邮箱查找用户
     * @param email 邮箱
     * @return Optional<User>
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return boolean
     */
    boolean existsByEmail(String email);

    /**
     * 根据邮箱查找用户，并立即加载其角色信息。
     * 使用 JOIN FETCH 可以避免 N+1 查询问题，并确保 roles 集合被初始化。
     * @param email 邮箱
     * @return Optional<User>
     */
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    /**
     * 统计拥有特定角色且状态为激活的用户数量。
     * 用于业务逻辑判断，例如：防止禁用最后一个管理员。
     * @param roleName 角色名称 (e.g., "ADMIN")
     * @param enabled  是否激活
     * @return 用户数量
     */
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName AND u.enabled = :enabled")
    long countByRoleNameAndEnabled(@Param("roleName") String roleName, @Param("enabled") boolean enabled);
}
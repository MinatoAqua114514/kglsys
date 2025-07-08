package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

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
}
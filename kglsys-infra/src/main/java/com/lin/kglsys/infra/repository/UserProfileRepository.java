package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * 根据用户昵称查找个人资料
     * @param nickname 用户昵称
     * @return Optional<UserProfile>
     */
    Optional<UserProfile> findByNickname(String nickname);

    /**
     * 根据用户昵称查找个人资料，并立即加载关联的User及其角色。
     * @param nickname 用户昵称
     * @return Optional<UserProfile>
     */
    @Query("SELECT up FROM UserProfile up JOIN FETCH up.user u JOIN FETCH u.roles WHERE up.nickname = :nickname")
    Optional<UserProfile> findByNicknameWithUserAndRoles(@Param("nickname") String nickname);
}
package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.UserAssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAssessmentAnswerRepository extends JpaRepository<UserAssessmentAnswer, Long> {

    List<UserAssessmentAnswer> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
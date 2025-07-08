package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.UserAssessmentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAssessmentAnswerRepository extends JpaRepository<UserAssessmentAnswer, Long> {
}
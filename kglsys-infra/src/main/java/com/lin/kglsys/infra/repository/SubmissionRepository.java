package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    // 未来可以根据需要添加自定义查询方法
}
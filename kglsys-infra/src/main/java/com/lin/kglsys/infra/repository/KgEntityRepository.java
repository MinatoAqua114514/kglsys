package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.KgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KgEntityRepository extends JpaRepository<KgEntity, Long> {
}
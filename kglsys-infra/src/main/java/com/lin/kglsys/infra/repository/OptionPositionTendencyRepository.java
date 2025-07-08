package com.lin.kglsys.infra.repository;

import com.lin.kglsys.domain.entity.OptionPositionTendency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionPositionTendencyRepository extends JpaRepository<OptionPositionTendency, Long> {

    /**
     * 根据选项ID列表，一次性查询所有相关的岗位倾向记录。
     * 使用 JOIN FETCH 预加载关联的 Position 实体，避免N+1查询。
     * @param optionIds 选项ID列表
     * @return 岗位倾向列表
     */
    @Query("SELECT opt FROM OptionPositionTendency opt JOIN FETCH opt.position WHERE opt.option.id IN :optionIds")
    List<OptionPositionTendency> findByOptionIdInWithDetails(List<Integer> optionIds);
}
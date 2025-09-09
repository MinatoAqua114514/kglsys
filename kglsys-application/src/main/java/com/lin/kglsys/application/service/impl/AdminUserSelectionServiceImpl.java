package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.AdminUserSelectionMapper;
import com.lin.kglsys.application.service.AdminUserSelectionService;
import com.lin.kglsys.domain.entity.UserLearningStatus;
import com.lin.kglsys.dto.response.UserSelectionViewDTO;
import com.lin.kglsys.infra.repository.UserLearningStatusRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserSelectionServiceImpl implements AdminUserSelectionService {

    private final UserLearningStatusRepository userLearningStatusRepository;
    private final AdminUserSelectionMapper userSelectionMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UserSelectionViewDTO> listUserSelections(Pageable pageable, String userKeyword, String positionKeyword) {
        Specification<UserLearningStatus> spec = (root, query, cb) -> {
            // 优化：在构建查询时，立即获取关联数据，防止N+1问题
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("user", JoinType.LEFT).fetch("userProfile", JoinType.LEFT);
                root.fetch("activePath", JoinType.LEFT).fetch("position", JoinType.LEFT);
            }

            List<Predicate> predicates = new ArrayList<>();

            // 核心条件：只查询已选择学习路径的用户
            predicates.add(cb.isNotNull(root.get("activePath")));

            // 用户关键字筛选
            if (StringUtils.hasText(userKeyword)) {
                String pattern = "%" + userKeyword.toLowerCase() + "%";
                Predicate emailPredicate = cb.like(cb.lower(root.get("user").get("email")), pattern);
                Predicate nicknamePredicate = cb.like(cb.lower(root.get("user").get("userProfile").get("nickname")), pattern);
                predicates.add(cb.or(emailPredicate, nicknamePredicate));
            }

            // 岗位关键字筛选
            if (StringUtils.hasText(positionKeyword)) {
                String pattern = "%" + positionKeyword.toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("activePath").get("position").get("displayName")), pattern));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<UserLearningStatus> statusPage = userLearningStatusRepository.findAll(spec, pageable);
        return statusPage.map(userSelectionMapper::toUserSelectionViewDTO);
    }
}
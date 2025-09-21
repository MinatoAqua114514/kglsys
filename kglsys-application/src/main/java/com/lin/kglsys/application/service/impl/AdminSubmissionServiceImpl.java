package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.AdminSubmissionMapper;
import com.lin.kglsys.application.service.AdminSubmissionService;
import com.lin.kglsys.domain.entity.Submission;
import com.lin.kglsys.dto.response.SubmissionViewDTO;
import com.lin.kglsys.infra.repository.SubmissionRepository;
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
public class AdminSubmissionServiceImpl implements AdminSubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AdminSubmissionMapper submissionMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SubmissionViewDTO> listSubmissions(Pageable pageable, String userEmail, String problemTitle) {
        Specification<Submission> spec = (root, query, cb) -> {
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("user", JoinType.LEFT);
                root.fetch("problem", JoinType.LEFT);
            }

            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(userEmail)) {
                predicates.add(cb.like(root.get("user").get("email"), "%" + userEmail + "%"));
            }
            if (StringUtils.hasText(problemTitle)) {
                predicates.add(cb.like(root.get("problem").get("title"), "%" + problemTitle + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Submission> submissionPage = submissionRepository.findAll(spec, pageable);
        return submissionPage.map(submissionMapper::toSubmissionViewDTO);
    }
}
package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.ProblemMapper;
import com.lin.kglsys.application.service.ProblemService;
import com.lin.kglsys.common.exception.business.ProblemNotFoundException;
import com.lin.kglsys.domain.entity.Problem;
import com.lin.kglsys.domain.entity.TestCase;
import com.lin.kglsys.dto.response.ProblemDetailDTO;
import com.lin.kglsys.infra.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;

    @Override
    @Transactional(readOnly = true)
    public ProblemDetailDTO getProblemDetails(Long problemId) {
        Problem problem = problemRepository.findByIdWithTestCases(problemId)
                .orElseThrow(ProblemNotFoundException::new);

        ProblemDetailDTO dto = problemMapper.toProblemDetailDTO(problem);

        // 手动映射并过滤出示例测试用例
        dto.setSampleTestCases(
                problem.getTestCases().stream()
                        .filter(TestCase::isSample)
                        .map(problemMapper::toTestCaseDTO)
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
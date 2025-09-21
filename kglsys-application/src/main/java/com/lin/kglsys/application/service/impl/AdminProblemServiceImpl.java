package com.lin.kglsys.application.service.impl;

import com.lin.kglsys.application.mapper.AdminProblemMapper;
import com.lin.kglsys.application.mapper.ProblemMapper;
import com.lin.kglsys.application.service.AdminProblemService;
import com.lin.kglsys.common.exception.business.InvalidParameterException;
import com.lin.kglsys.common.exception.business.ResourceNotFoundException;
import com.lin.kglsys.domain.entity.Problem;
import com.lin.kglsys.domain.entity.TestCase;
import com.lin.kglsys.dto.request.ProblemSaveDTO;
import com.lin.kglsys.dto.request.TestCaseSaveDTO;
import com.lin.kglsys.dto.response.ProblemDetailDTO;
import com.lin.kglsys.infra.repository.ProblemEntityRepository;
import com.lin.kglsys.infra.repository.ProblemRepository;
import com.lin.kglsys.infra.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProblemServiceImpl implements AdminProblemService {

    private final ProblemRepository problemRepository;
    private final TestCaseRepository testCaseRepository;
    private final ProblemEntityRepository problemEntityRepository;
    private final AdminProblemMapper adminProblemMapper;
    private final ProblemMapper problemMapper; // 复用查询DTO转换

    @Override
    @Transactional(readOnly = true)
    public Page<ProblemDetailDTO> listProblems(Pageable pageable, String title) {
        // 简单实现，可后续通过Specification扩展
        Page<Problem> problemPage = problemRepository.findAll(pageable);
        return problemPage.map(problemMapper::toProblemDetailDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProblemDetailDTO getProblemById(Long id) {
        return problemRepository.findByIdWithTestCases(id)
                .map(problemMapper::toProblemDetailDTO)
                .orElseThrow(() -> new ResourceNotFoundException("编程题目不存在"));
    }

    @Override
    @Transactional
    public ProblemDetailDTO createProblem(ProblemSaveDTO dto) {
        Problem problem = adminProblemMapper.fromSaveDTO(dto);
        LocalDateTime now = LocalDateTime.now();
        problem.setCreatedAt(now);
        problem.setUpdatedAt(now);
        Problem savedProblem = problemRepository.save(problem);

        Set<TestCase> testCases = dto.getTestCases().stream().map(tcDto -> {
            TestCase testCase = adminProblemMapper.testCaseFromSaveDTO(tcDto);
            testCase.setProblem(savedProblem);
            return testCase;
        }).collect(Collectors.toSet());
        testCaseRepository.saveAll(testCases);

        savedProblem.setTestCases(testCases);
        return problemMapper.toProblemDetailDTO(savedProblem);
    }

    @Override
    @Transactional
    public ProblemDetailDTO updateProblem(Long id, ProblemSaveDTO dto) {
        Problem problem = problemRepository.findByIdWithTestCases(id)
                .orElseThrow(() -> new ResourceNotFoundException("要更新的编程题目不存在"));

        adminProblemMapper.updateFromSaveDTO(dto, problem);
        problem.setUpdatedAt(LocalDateTime.now());

        updateTestCasesForProblem(problem, dto.getTestCases());

        Problem updatedProblem = problemRepository.save(problem);
        return problemMapper.toProblemDetailDTO(updatedProblem);
    }

    @Override
    @Transactional
    public void deleteProblem(Long id) {
        if (!problemRepository.existsById(id)) {
            throw new ResourceNotFoundException("要删除的编程题目不存在");
        }
        // 业务检查：如果题目被知识实体关联，则不允许删除
        if (problemEntityRepository.existsByProblemId(id)) {
            throw new InvalidParameterException("无法删除：该题目已被知识实体关联");
        }
        problemRepository.deleteById(id);
    }

    private void updateTestCasesForProblem(Problem problem, List<TestCaseSaveDTO> tcDtos) {
        Map<Long, TestCaseSaveDTO> dtoMap = tcDtos.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(TestCaseSaveDTO::getId, Function.identity()));

        // 删除
        Set<TestCase> toDelete = problem.getTestCases().stream()
                .filter(tc -> !dtoMap.containsKey(tc.getId()))
                .collect(Collectors.toSet());
        problem.getTestCases().removeAll(toDelete);
        testCaseRepository.deleteAll(toDelete);

        // 更新和新增
        for (TestCaseSaveDTO dto : tcDtos) {
            if (dto.getId() != null) { // 更新
                TestCase tcToUpdate = problem.getTestCases().stream()
                        .filter(tc -> tc.getId().equals(dto.getId())).findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("测试用例不存在: " + dto.getId()));
                adminProblemMapper.updateTestCaseFromSaveDTO(dto, tcToUpdate);
            } else { // 新增
                TestCase newTestCase = adminProblemMapper.testCaseFromSaveDTO(dto);
                newTestCase.setProblem(problem);
                problem.getTestCases().add(newTestCase);
            }
        }
    }
}
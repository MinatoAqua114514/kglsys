package com.lin.kglsys.dto.request;

import com.lin.kglsys.domain.valobj.ProblemDifficulty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProblemSaveDTO {

    @NotBlank(message = "题目不能为空")
    private String title;

    @NotBlank(message = "题目描述不能为空")
    private String description;

    @NotNull(message = "难度不能为空")
    private ProblemDifficulty difficulty;

    @NotNull(message = "时间限制不能为空")
    private Integer timeLimitMs;

    @NotNull(message = "内存限制不能为空")
    private Integer memoryLimitKb;

    private List<String> tags; // 标签列表

    @NotEmpty(message = "至少需要一个测试用例")
    @Valid // 级联校验
    private List<TestCaseSaveDTO> testCases;
}
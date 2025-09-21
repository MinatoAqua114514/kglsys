package com.lin.kglsys.dto.response;

import com.lin.kglsys.domain.valobj.SubmissionStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubmissionViewDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private Long problemId;
    private String problemTitle;
    private String language;
    private SubmissionStatus status;
    private Integer executionTimeMs;
    private Integer memoryUsedKb;
    private LocalDateTime submittedAt;
    // 为安全起见，默认不返回源代码，如有需要可单独提供接口
}
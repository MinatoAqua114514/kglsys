package com.lin.kglsys.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeSubmissionResponse {
    private String submissionId; // 返回唯一的提交ID，用于前端监听WebSocket
}
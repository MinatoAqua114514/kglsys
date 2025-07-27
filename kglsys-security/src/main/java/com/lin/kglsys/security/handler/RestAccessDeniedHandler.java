package com.lin.kglsys.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.kglsys.common.constant.ResultCode;
import com.lin.kglsys.common.response.ApiResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("Access denied: {}", accessDeniedException.getMessage());

        ApiResult<Void> result = ApiResult.failure(ResultCode.PERMISSION_DENIED);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 设置HTTP状态码为403
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
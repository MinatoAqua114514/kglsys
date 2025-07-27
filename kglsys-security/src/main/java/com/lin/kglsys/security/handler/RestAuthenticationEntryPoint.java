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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("Authentication failed: {}", authException.getMessage());

        // 根据异常类型返回更具体的信息
        // BadCredentialsException 是用户名或密码错误的常见异常
        ResultCode resultCode = ResultCode.AUTHENTICATION_FAILED;
        ApiResult<Void> result = ApiResult.failure(resultCode);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置HTTP状态码为401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
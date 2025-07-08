package com.lin.kglsys.security.filter;

import com.lin.kglsys.common.utils.UserContextHolder;
import com.lin.kglsys.domain.entity.User;
import com.lin.kglsys.security.jwt.JwtTokenProvider;
import com.lin.kglsys.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                String username = jwtProvider.getUsernameFromJWT(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // [新增] 将用户ID存入ThreadLocal
                if (userDetails instanceof User) {
                    UserContextHolder.setUserId(((User) userDetails).getId());
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("无法在安全上下文中设置用户认证", ex);
        } finally {
            // [新增] 在请求结束时清理ThreadLocal，防止内存泄漏
            if (isAsyncDispatch(request)) {
                filterChain.doFilter(request, response);
            } else {
                try {
                    filterChain.doFilter(request, response);
                } finally {
                    UserContextHolder.clear();
                }
            }
        }
    }

    private void doFilterAndClear(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            try {
                // 1. 从 HTTP 请求的 Authorization 头部获取 JWT（如果存在）
                String jwt = getJwtFromRequest(request);

                // 2. 如果 token 非空且合法，继续处理
                if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                    // 3. 从 JWT 中提取用户名
                    String username = jwtProvider.getUsernameFromJWT(jwt);

                    // 4. 通过用户名加载用户详细信息（如权限）
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    // 5. 构造认证对象，包含用户、凭证（此处为 null）和权限
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    // 6. 设置认证的附加信息（如 IP 地址、session ID）
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 7. 将认证信息设置到 SecurityContext 上下文，完成认证过程
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception ex) {
                // 8. 处理异常（如 token 无效、解析失败等），打印日志但不中断请求流程
                log.error("无法在安全上下文中设置用户认证", ex);
            }
        } finally {
            UserContextHolder.clear();
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
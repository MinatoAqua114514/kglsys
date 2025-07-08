package com.lin.kglsys.security.jwt;

import com.lin.kglsys.domain.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey secretKey;


    /**
     * 【安全强化】: 使用 @PostConstruct 在依赖注入完成后初始化密钥。
     * 密钥应从环境变量或安全的配置中心获取，并进行 Base64 编码以增加安全性。
     * 此处假设配置文件中的密钥是 Base64 编码的。
     */
    @PostConstruct
    protected void init() {
        // 检查密钥长度，HMAC-SHA512 推荐使用至少 64 字节 (512位) 的密钥
        byte[] secretBytes = Base64.getDecoder().decode(jwtSecret);
        if (secretBytes.length < 64) {
            log.warn("Warning: The configured JWT secret is shorter than 64 bytes, which is not recommended for HS512.");
        }
        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
    }

    /**
     * 根据用户信息生成JWT
     * @param user 用户实体
     * @return JWT字符串
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // 从User实体中的Role集合获取权限字符串
        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName()) // 添加 "ROLE_" 前缀，符合Spring Security规范
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", roles) // 将角色信息存入claims
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(this.secretKey)
                .compact();
    }

    /**
     * 从 JWT 中解析出用户名。
     * @param token JWT 字符串。
     * @return 用户名。
     */
    public String getUsernameFromJWT(String token) {
        // 1. 使用签名密钥解析 token 并获取其 claims 内容
        // 2. 从 claims 中获取 subject，即用户名
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * 校验 JWT 的有效性。
     * @param authToken JWT 字符串。
     * @return 如果 token 有效则返回 true，否则返回 false。
     */
    public boolean validateToken(String authToken) {
        try {
            // 1. 使用密钥验证 token 的签名是否正确，格式是否符合预期
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(authToken); // 如果解析成功，token 是合法的
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage()); // <--- 使用 log
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage()); // <--- 使用 log
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage()); // <--- 使用 log
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage()); // <--- 使用 log
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage()); // <--- 使用 log
        }
        // token 验证失败
        return false;
    }
}
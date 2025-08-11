package com.lin.kglsys.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AdminUserViewDTO {
    private Long id;
    private String email;
    private String nickname;
    private Set<String> roles;
    private boolean enabled;
    private LocalDateTime createdAt;
}
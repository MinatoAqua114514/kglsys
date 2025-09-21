package com.lin.kglsys.dto.response;

import com.lin.kglsys.domain.valobj.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private UserRole role; // 新增：返回用户角色
}

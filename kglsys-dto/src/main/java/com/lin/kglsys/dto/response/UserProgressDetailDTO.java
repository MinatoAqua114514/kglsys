package com.lin.kglsys.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class UserProgressDetailDTO {
    private Long userId;
    private String userEmail;
    private String userNickname;
    private Long activePathId;
    private String activePathName;
    private List<UserProgressNodeDTO> progressNodes;
}
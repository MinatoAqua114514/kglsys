package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.EntityLinkRequestDTO;
import com.lin.kglsys.dto.response.LinkedContentDTO;

public interface AdminEntityContentService {
    LinkedContentDTO getLinkedContent(Long entityId);
    LinkedContentDTO updateLinkedContent(Long entityId, EntityLinkRequestDTO dto);
}
package com.lin.kglsys.application.service;

import com.lin.kglsys.domain.valobj.UserLearningProgressStatus;

public interface UserProgressService {

    /**
     * 更新指定节点的学习进度
     * @param nodeId 学习路径节点ID
     * @param newStatus 新的学习状态
     */
    void updateNodeProgress(Long nodeId, UserLearningProgressStatus newStatus);
}
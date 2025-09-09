package com.lin.kglsys.application.service;

import com.lin.kglsys.dto.request.LearningPathSaveDTO;
import com.lin.kglsys.dto.response.LearningPathDTO;

import java.util.List;

public interface AdminLearningPathService {

    List<LearningPathDTO> listPathsForPosition(Integer positionId);

    LearningPathDTO getPathById(Long pathId);

    LearningPathDTO createPath(Integer positionId, LearningPathSaveDTO dto);

    LearningPathDTO updatePath(Long pathId, LearningPathSaveDTO dto);

    void deletePath(Long pathId);
}
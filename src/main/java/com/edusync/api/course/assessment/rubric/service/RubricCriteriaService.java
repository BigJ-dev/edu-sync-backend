package com.edusync.api.course.assessment.rubric.service;

import com.edusync.api.course.assessment.rubric.dto.RubricCriteriaRequest;
import com.edusync.api.course.assessment.rubric.dto.RubricCriteriaResponse;
import com.edusync.api.course.assessment.rubric.model.RubricCriteria;

import java.util.List;
import java.util.UUID;

public interface RubricCriteriaService {

    RubricCriteriaResponse createRubricCriteria(UUID assessmentUuid, RubricCriteriaRequest.Create request);

    List<RubricCriteriaResponse> findAllRubricCriteriaByAssessment(UUID assessmentUuid);

    RubricCriteriaResponse updateRubricCriteria(UUID criteriaUuid, RubricCriteriaRequest.Update request);

    void deleteRubricCriteria(UUID criteriaUuid);

    RubricCriteria findRubricCriteriaEntityByUuid(UUID uuid);
}

package com.edusync.api.course.assessment.rubric.controller;

import com.edusync.api.course.assessment.rubric.controller.api.RubricCriteriaApi;
import com.edusync.api.course.assessment.rubric.dto.RubricCriteriaRequest;
import com.edusync.api.course.assessment.rubric.dto.RubricCriteriaResponse;
import com.edusync.api.course.assessment.rubric.service.RubricCriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class RubricCriteriaController implements RubricCriteriaApi {

    private final RubricCriteriaService service;

    @Override
    public RubricCriteriaResponse create(UUID assessmentUuid, RubricCriteriaRequest.Create request) {
        return service.create(assessmentUuid, request);
    }

    @Override
    public List<RubricCriteriaResponse> findAllByAssessment(UUID assessmentUuid) {
        return service.findAllByAssessment(assessmentUuid);
    }

    @Override
    public RubricCriteriaResponse update(UUID assessmentUuid, UUID criteriaUuid, RubricCriteriaRequest.Update request) {
        return service.update(criteriaUuid, request);
    }

    @Override
    public void delete(UUID assessmentUuid, UUID criteriaUuid) {
        service.delete(criteriaUuid);
    }
}

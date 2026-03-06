package com.edusync.api.course.assessment.grade.controller;

import com.edusync.api.course.assessment.grade.controller.api.RubricGradeApi;
import com.edusync.api.course.assessment.grade.dto.RubricGradeRequest;
import com.edusync.api.course.assessment.grade.dto.RubricGradeResponse;
import com.edusync.api.course.assessment.grade.service.RubricGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class RubricGradeController implements RubricGradeApi {

    private final RubricGradeService service;

    @Override
    public RubricGradeResponse award(UUID submissionUuid, UUID criteriaUuid, RubricGradeRequest.Award request) {
        return service.award(submissionUuid, criteriaUuid, request);
    }

    @Override
    public List<RubricGradeResponse> findAllBySubmission(UUID submissionUuid) {
        return service.findAllBySubmission(submissionUuid);
    }
}

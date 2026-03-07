package com.edusync.api.course.assessment.submission.controller;

import com.edusync.api.course.assessment.submission.controller.api.SubmissionApi;
import com.edusync.api.course.assessment.submission.dto.SubmissionRequest;
import com.edusync.api.course.assessment.submission.dto.SubmissionResponse;
import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import com.edusync.api.course.assessment.submission.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class SubmissionController implements SubmissionApi {

    private final SubmissionService service;

    @Override
    public SubmissionResponse submit(UUID assessmentUuid, SubmissionRequest.Submit request) {
        return service.submitAssessment(assessmentUuid, request);
    }

    @Override
    public List<SubmissionResponse> findAllByAssessment(UUID assessmentUuid, SubmissionStatus status, Boolean late) {
        return service.findAllSubmissionsByAssessment(assessmentUuid, status, late);
    }

    @Override
    public SubmissionResponse findByUuid(UUID assessmentUuid, UUID submissionUuid) {
        return service.findSubmissionByUuid(submissionUuid);
    }

    @Override
    public SubmissionResponse grade(UUID assessmentUuid, UUID submissionUuid, SubmissionRequest.Grade request) {
        return service.gradeSubmission(submissionUuid, request);
    }

    @Override
    public SubmissionResponse returnForResubmission(UUID assessmentUuid, UUID submissionUuid, SubmissionRequest.Return request) {
        return service.returnSubmissionForResubmission(submissionUuid, request);
    }
}

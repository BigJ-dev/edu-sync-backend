package com.edusync.api.course.assessment.submission.service;

import com.edusync.api.course.assessment.submission.dto.SubmissionRequest;
import com.edusync.api.course.assessment.submission.dto.SubmissionResponse;
import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;

import java.util.List;
import java.util.UUID;

public interface SubmissionService {

    SubmissionResponse submitAssessment(UUID assessmentUuid, SubmissionRequest.Submit request);

    List<SubmissionResponse> findAllSubmissionsByAssessment(UUID assessmentUuid, SubmissionStatus status, Boolean late);

    SubmissionResponse findSubmissionByUuid(UUID submissionUuid);

    SubmissionResponse gradeSubmission(UUID submissionUuid, SubmissionRequest.Grade request);

    SubmissionResponse returnSubmissionForResubmission(UUID submissionUuid, SubmissionRequest.Return request);

    AssessmentSubmission findSubmissionEntityByUuid(UUID uuid);
}

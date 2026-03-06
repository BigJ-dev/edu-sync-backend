package com.edusync.api.course.assessment.submission.dto;

import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SubmissionResponse(
        UUID uuid,
        UUID assessmentUuid,
        UUID studentUuid,
        String submissionText,
        String s3Key,
        String fileName,
        Long fileSizeBytes,
        String mimeType,
        Instant submittedAt,
        boolean isLate,
        BigDecimal marksObtained,
        String feedback,
        UUID gradedByUuid,
        Instant gradedAt,
        SubmissionStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static SubmissionResponse from(AssessmentSubmission submission) {
        return new SubmissionResponse(
                submission.getUuid(),
                submission.getAssessment().getUuid(),
                submission.getStudent().getUuid(),
                submission.getSubmissionText(),
                submission.getS3Key(),
                submission.getFileName(),
                submission.getFileSizeBytes(),
                submission.getMimeType(),
                submission.getSubmittedAt(),
                submission.isLate(),
                submission.getMarksObtained(),
                submission.getFeedback(),
                submission.getGradedBy() != null ? submission.getGradedBy().getUuid() : null,
                submission.getGradedAt(),
                submission.getStatus(),
                submission.getCreatedAt(),
                submission.getUpdatedAt()
        );
    }
}

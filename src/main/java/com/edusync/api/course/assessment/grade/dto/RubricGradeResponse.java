package com.edusync.api.course.assessment.grade.dto;

import com.edusync.api.course.assessment.grade.model.RubricGrade;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RubricGradeResponse(
        Long id,
        UUID submissionUuid,
        UUID criteriaUuid,
        BigDecimal marksAwarded,
        String comment,
        UUID gradedByUuid,
        Instant gradedAt
) {
    public static RubricGradeResponse from(RubricGrade grade) {
        return new RubricGradeResponse(
                grade.getId(),
                grade.getSubmission().getUuid(),
                grade.getCriteria().getUuid(),
                grade.getMarksAwarded(),
                grade.getComment(),
                grade.getGradedBy() != null ? grade.getGradedBy().getUuid() : null,
                grade.getGradedAt()
        );
    }
}

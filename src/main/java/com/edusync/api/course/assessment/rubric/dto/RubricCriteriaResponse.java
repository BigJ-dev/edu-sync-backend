package com.edusync.api.course.assessment.rubric.dto;

import com.edusync.api.course.assessment.rubric.model.RubricCriteria;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RubricCriteriaResponse(
        UUID uuid,
        UUID assessmentUuid,
        String title,
        String description,
        BigDecimal maxMarks,
        int sortOrder,
        Instant createdAt
) {
    public static RubricCriteriaResponse from(RubricCriteria criteria) {
        return new RubricCriteriaResponse(
                criteria.getUuid(),
                criteria.getAssessment().getUuid(),
                criteria.getTitle(),
                criteria.getDescription(),
                criteria.getMaxMarks(),
                criteria.getSortOrder(),
                criteria.getCreatedAt()
        );
    }
}

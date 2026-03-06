package com.edusync.api.course.assessment.common.dto;

import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.common.enums.DeliveryMode;
import com.edusync.api.course.assessment.common.model.Assessment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AssessmentResponse(
        UUID uuid,
        UUID moduleUuid,
        UUID createdByUuid,
        String title,
        String description,
        AssessmentType assessmentType,
        DeliveryMode deliveryMode,
        BigDecimal totalMarks,
        BigDecimal weightPct,
        Instant dueDate,
        Instant visibleFrom,
        boolean allowLateSubmission,
        BigDecimal latePenaltyPct,
        String briefS3Key,
        String briefFileName,
        AssessmentStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static AssessmentResponse from(Assessment assessment) {
        return new AssessmentResponse(
                assessment.getUuid(),
                assessment.getModule().getUuid(),
                assessment.getCreatedBy().getUuid(),
                assessment.getTitle(),
                assessment.getDescription(),
                assessment.getAssessmentType(),
                assessment.getDeliveryMode(),
                assessment.getTotalMarks(),
                assessment.getWeightPct(),
                assessment.getDueDate(),
                assessment.getVisibleFrom(),
                assessment.isAllowLateSubmission(),
                assessment.getLatePenaltyPct(),
                assessment.getBriefS3Key(),
                assessment.getBriefFileName(),
                assessment.getStatus(),
                assessment.getCreatedAt(),
                assessment.getUpdatedAt()
        );
    }
}

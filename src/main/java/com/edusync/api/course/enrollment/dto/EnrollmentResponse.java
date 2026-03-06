package com.edusync.api.course.enrollment.dto;

import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.model.CourseEnrollment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record EnrollmentResponse(
        Long id,
        UUID courseUuid,
        UUID studentUuid,
        Instant enrolledAt,
        EnrollmentStatus status,
        BigDecimal finalAttendancePct,
        BigDecimal finalGrade,
        Instant withdrawnAt,
        String withdrawalReason,
        boolean isBlocked,
        Instant blockedAt,
        String blockedReason
) {
    public static EnrollmentResponse from(CourseEnrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourse().getUuid(),
                enrollment.getStudent().getUuid(),
                enrollment.getEnrolledAt(),
                enrollment.getStatus(),
                enrollment.getFinalAttendancePct(),
                enrollment.getFinalGrade(),
                enrollment.getWithdrawnAt(),
                enrollment.getWithdrawalReason(),
                enrollment.isBlocked(),
                enrollment.getBlockedAt(),
                enrollment.getBlockedReason()
        );
    }
}

package com.edusync.api.dashboard.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record StudentCourseSummary(
        UUID courseUuid,
        String courseCode,
        String courseTitle,
        BigDecimal attendancePct,
        BigDecimal averageGrade,
        long pendingAssessments
) {}

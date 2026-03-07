package com.edusync.api.transcript.dto;

import com.edusync.api.course.enrollment.enums.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TranscriptCourseEntry(
        UUID courseUuid,
        String courseCode,
        String courseTitle,
        EnrollmentStatus enrollmentStatus,
        Instant enrolledAt,
        BigDecimal attendancePct,
        BigDecimal averageGrade,
        BigDecimal finalGrade,
        List<TranscriptAssessmentEntry> assessments
) {}

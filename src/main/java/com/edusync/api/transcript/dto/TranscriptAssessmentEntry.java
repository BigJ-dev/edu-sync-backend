package com.edusync.api.transcript.dto;

import com.edusync.api.course.assessment.common.enums.AssessmentType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TranscriptAssessmentEntry(
        UUID assessmentUuid,
        String assessmentTitle,
        AssessmentType assessmentType,
        String moduleTitle,
        BigDecimal totalMarks,
        BigDecimal marksObtained,
        BigDecimal weightPct,
        Instant submittedAt,
        boolean isLate
) {}

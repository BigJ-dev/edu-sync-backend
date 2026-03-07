package com.edusync.api.work.dto;

import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record StudentAssessmentItem(
        UUID assessmentUuid,
        String courseCode,
        String courseTitle,
        String moduleTitle,
        String title,
        AssessmentType assessmentType,
        BigDecimal totalMarks,
        Instant dueDate,
        AssessmentStatus status,
        boolean submitted,
        SubmissionStatus submissionStatus,
        BigDecimal marksObtained,
        Instant submittedAt
) {}

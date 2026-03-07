package com.edusync.api.work.dto;

import java.util.List;
import java.util.UUID;

public record StudentWorkResponse(
        UUID studentUuid,
        List<StudentAssessmentItem> assessments,
        List<StudentQuizItem> quizzes,
        int totalAssessments,
        int totalQuizzes
) {}

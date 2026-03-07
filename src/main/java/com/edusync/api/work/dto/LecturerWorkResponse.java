package com.edusync.api.work.dto;

import com.edusync.api.course.assessment.common.dto.AssessmentResponse;
import com.edusync.api.course.quiz.common.dto.QuizResponse;

import java.util.List;
import java.util.UUID;

public record LecturerWorkResponse(
        UUID lecturerUuid,
        List<AssessmentResponse> assessments,
        List<QuizResponse> quizzes,
        int totalAssessments,
        int totalQuizzes
) {}

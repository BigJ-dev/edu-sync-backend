package com.edusync.api.work.dto;

import com.edusync.api.course.quiz.attempt.enums.AttemptStatus;
import com.edusync.api.course.quiz.common.enums.QuizStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record StudentQuizItem(
        UUID quizUuid,
        String courseCode,
        String courseTitle,
        String moduleTitle,
        String title,
        BigDecimal totalMarks,
        Integer timeLimitMinutes,
        int maxAttempts,
        Instant visibleFrom,
        Instant visibleUntil,
        QuizStatus status,
        int attemptsTaken,
        BigDecimal bestScore,
        BigDecimal bestScorePct,
        Boolean passed,
        AttemptStatus latestAttemptStatus
) {}

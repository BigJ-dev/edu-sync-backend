package com.edusync.api.course.quiz.attempt.dto;

import com.edusync.api.course.quiz.attempt.enums.AttemptStatus;
import com.edusync.api.course.quiz.attempt.model.QuizAttempt;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AttemptResponse(
        UUID uuid,
        UUID quizUuid,
        UUID studentUuid,
        int attemptNumber,
        Instant startedAt,
        Instant completedAt,
        BigDecimal score,
        BigDecimal scorePct,
        Boolean passed,
        AttemptStatus status,
        Instant createdAt
) {
    public static AttemptResponse from(QuizAttempt attempt) {
        return new AttemptResponse(
                attempt.getUuid(),
                attempt.getQuiz().getUuid(),
                attempt.getStudent().getUuid(),
                attempt.getAttemptNumber(),
                attempt.getStartedAt(),
                attempt.getCompletedAt(),
                attempt.getScore(),
                attempt.getScorePct(),
                attempt.getPassed(),
                attempt.getStatus(),
                attempt.getCreatedAt()
        );
    }
}

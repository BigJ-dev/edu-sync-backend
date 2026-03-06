package com.edusync.api.course.quiz.common.dto;

import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.course.quiz.common.model.Quiz;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record QuizResponse(
        UUID uuid,
        UUID moduleUuid,
        UUID classSessionUuid,
        UUID createdByUuid,
        String title,
        String description,
        Integer timeLimitMinutes,
        BigDecimal totalMarks,
        BigDecimal passMarkPct,
        BigDecimal weightPct,
        int maxAttempts,
        boolean shuffleQuestions,
        boolean showAnswersAfter,
        String documentS3Key,
        String documentName,
        Instant visibleFrom,
        Instant visibleUntil,
        QuizStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static QuizResponse from(Quiz quiz) {
        return new QuizResponse(
                quiz.getUuid(),
                quiz.getModule().getUuid(),
                quiz.getClassSession() != null ? quiz.getClassSession().getUuid() : null,
                quiz.getCreatedBy().getUuid(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getTimeLimitMinutes(),
                quiz.getTotalMarks(),
                quiz.getPassMarkPct(),
                quiz.getWeightPct(),
                quiz.getMaxAttempts(),
                quiz.isShuffleQuestions(),
                quiz.isShowAnswersAfter(),
                quiz.getDocumentS3Key(),
                quiz.getDocumentName(),
                quiz.getVisibleFrom(),
                quiz.getVisibleUntil(),
                quiz.getStatus(),
                quiz.getCreatedAt(),
                quiz.getUpdatedAt()
        );
    }
}

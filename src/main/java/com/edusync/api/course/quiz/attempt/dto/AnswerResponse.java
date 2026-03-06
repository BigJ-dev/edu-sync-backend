package com.edusync.api.course.quiz.attempt.dto;

import com.edusync.api.course.quiz.attempt.model.QuizAnswer;
import com.edusync.api.course.quiz.attempt.model.QuizAnswerOption;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AnswerResponse(
        UUID questionUuid,
        UUID selectedOptionUuid,
        List<UUID> selectedOptionUuids,
        String answerText,
        Boolean isCorrect,
        BigDecimal marksAwarded,
        boolean requiresManualGrading,
        Instant answeredAt
) {
    public static AnswerResponse from(QuizAnswer answer, List<QuizAnswerOption> answerOptions) {
        return new AnswerResponse(
                answer.getQuestion().getUuid(),
                answer.getSelectedOption() != null ? answer.getSelectedOption().getUuid() : null,
                answerOptions.stream().map(ao -> ao.getOption().getUuid()).toList(),
                answer.getAnswerText(),
                answer.getIsCorrect(),
                answer.getMarksAwarded(),
                answer.isRequiresManualGrading(),
                answer.getAnsweredAt()
        );
    }
}

package com.edusync.api.course.quiz.question.dto;

import com.edusync.api.course.quiz.question.enums.QuestionType;
import com.edusync.api.course.quiz.question.model.QuizQuestion;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record QuestionResponse(
        UUID uuid,
        UUID quizUuid,
        String questionText,
        QuestionType questionType,
        String imageS3Key,
        BigDecimal marks,
        int sortOrder,
        String explanation,
        List<OptionResponse> options,
        Instant createdAt,
        Instant updatedAt
) {
    public static QuestionResponse from(QuizQuestion question, List<OptionResponse> options) {
        return new QuestionResponse(
                question.getUuid(),
                question.getQuiz().getUuid(),
                question.getQuestionText(),
                question.getQuestionType(),
                question.getImageS3Key(),
                question.getMarks(),
                question.getSortOrder(),
                question.getExplanation(),
                options,
                question.getCreatedAt(),
                question.getUpdatedAt()
        );
    }
}

package com.edusync.api.course.quiz.question.dto;

import com.edusync.api.course.quiz.question.model.QuizQuestionOption;

import java.util.UUID;

public record OptionResponse(
        UUID uuid,
        String optionText,
        boolean isCorrect,
        String feedback,
        int sortOrder
) {
    public static OptionResponse from(QuizQuestionOption option) {
        return new OptionResponse(
                option.getUuid(),
                option.getOptionText(),
                option.isCorrect(),
                option.getFeedback(),
                option.getSortOrder()
        );
    }
}

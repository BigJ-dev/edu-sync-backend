package com.edusync.api.course.quiz.question.dto;

import com.edusync.api.course.quiz.question.enums.QuestionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public sealed interface QuestionRequest {

    @Schema(name = "QuestionCreate")
    record Create(
            @NotBlank(message = "Question text is required")
            String questionText,

            @NotNull(message = "Question type is required")
            QuestionType questionType,

            @Size(max = 500, message = "Image S3 key must not exceed 500 characters")
            String imageS3Key,

            @NotNull(message = "Marks is required")
            @DecimalMin(value = "0.01", message = "Marks must be at least 0.01")
            BigDecimal marks,

            @NotNull(message = "Sort order is required")
            @Min(value = 0, message = "Sort order must be at least 0")
            Integer sortOrder,

            String explanation,

            @Valid
            List<OptionCreate> options
    ) implements QuestionRequest {}

    @Schema(name = "QuestionUpdate")
    record Update(
            @NotBlank(message = "Question text is required")
            String questionText,

            @Size(max = 500, message = "Image S3 key must not exceed 500 characters")
            String imageS3Key,

            @NotNull(message = "Marks is required")
            @DecimalMin(value = "0.01", message = "Marks must be at least 0.01")
            BigDecimal marks,

            @NotNull(message = "Sort order is required")
            @Min(value = 0, message = "Sort order must be at least 0")
            Integer sortOrder,

            String explanation
    ) implements QuestionRequest {}

    @Schema(name = "QuestionOptionCreate")
    record OptionCreate(
            @NotBlank(message = "Option text is required")
            @Size(max = 1000, message = "Option text must not exceed 1000 characters")
            String optionText,

            boolean isCorrect,

            String feedback,

            @Min(value = 0, message = "Sort order must be at least 0")
            int sortOrder
    ) {}
}

package com.edusync.api.course.quiz.common.dto;

import com.edusync.api.course.quiz.common.enums.QuizStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public sealed interface QuizRequest {

    @Schema(name = "QuizCreate")
    record Create(
            @NotNull(message = "Module UUID is required")
            UUID moduleUuid,

            UUID classSessionUuid,

            @NotNull(message = "Created by UUID is required")
            UUID createdByUuid,

            @NotBlank(message = "Quiz title is required")
            @Size(max = 255, message = "Quiz title must not exceed 255 characters")
            String title,

            String description,

            @Min(value = 1, message = "Time limit must be at least 1 minute")
            Integer timeLimitMinutes,

            @NotNull(message = "Total marks is required")
            @DecimalMin(value = "0.01", message = "Total marks must be at least 0.01")
            BigDecimal totalMarks,

            @DecimalMin(value = "0", message = "Pass mark percentage must be between 0 and 100")
            @DecimalMax(value = "100", message = "Pass mark percentage must be between 0 and 100")
            BigDecimal passMarkPct,

            @DecimalMin(value = "0", message = "Weight percentage must be between 0 and 100")
            @DecimalMax(value = "100", message = "Weight percentage must be between 0 and 100")
            BigDecimal weightPct,

            @Min(value = 1, message = "Max attempts must be at least 1")
            Integer maxAttempts,

            Boolean shuffleQuestions,

            Boolean showAnswersAfter,

            @Size(max = 500, message = "Document S3 key must not exceed 500 characters")
            String documentS3Key,

            @Size(max = 255, message = "Document name must not exceed 255 characters")
            String documentName,

            Instant visibleFrom,

            Instant visibleUntil
    ) implements QuizRequest {}

    @Schema(name = "QuizUpdate")
    record Update(
            @NotBlank(message = "Quiz title is required")
            @Size(max = 255, message = "Quiz title must not exceed 255 characters")
            String title,

            String description,

            @Min(value = 1, message = "Time limit must be at least 1 minute")
            Integer timeLimitMinutes,

            @NotNull(message = "Total marks is required")
            @DecimalMin(value = "0.01", message = "Total marks must be at least 0.01")
            BigDecimal totalMarks,

            @DecimalMin(value = "0", message = "Pass mark percentage must be between 0 and 100")
            @DecimalMax(value = "100", message = "Pass mark percentage must be between 0 and 100")
            BigDecimal passMarkPct,

            @DecimalMin(value = "0", message = "Weight percentage must be between 0 and 100")
            @DecimalMax(value = "100", message = "Weight percentage must be between 0 and 100")
            BigDecimal weightPct,

            @Min(value = 1, message = "Max attempts must be at least 1")
            Integer maxAttempts,

            Boolean shuffleQuestions,

            Boolean showAnswersAfter,

            @Size(max = 500, message = "Document S3 key must not exceed 500 characters")
            String documentS3Key,

            @Size(max = 255, message = "Document name must not exceed 255 characters")
            String documentName,

            Instant visibleFrom,

            Instant visibleUntil
    ) implements QuizRequest {}

    @Schema(name = "QuizUpdateStatus")
    record UpdateStatus(
            @NotNull(message = "Quiz status is required")
            QuizStatus status
    ) implements QuizRequest {}

    @Schema(name = "QuizReopen", description = "Reopen a closed quiz with new visibility window so students can attempt it again.")
    record Reopen(
            @NotNull(message = "Visible from is required")
            Instant visibleFrom,

            @NotNull(message = "Visible until is required")
            Instant visibleUntil,

            @Min(value = 1, message = "Max attempts must be at least 1")
            Integer additionalAttempts
    ) implements QuizRequest {}
}

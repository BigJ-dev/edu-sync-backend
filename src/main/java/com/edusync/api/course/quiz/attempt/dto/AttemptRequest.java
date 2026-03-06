package com.edusync.api.course.quiz.attempt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public sealed interface AttemptRequest {

    @Schema(name = "AttemptStart")
    record Start(
            @NotNull(message = "Student UUID is required")
            UUID studentUuid
    ) implements AttemptRequest {}

    @Schema(name = "AttemptSubmitAnswer")
    record SubmitAnswer(
            @NotNull(message = "Question UUID is required")
            UUID questionUuid,

            UUID selectedOptionUuid,

            List<UUID> selectedOptionUuids,

            @Size(max = 2000, message = "Answer text must not exceed 2000 characters")
            String answerText
    ) implements AttemptRequest {}

    @Schema(name = "AttemptComplete")
    record Complete() implements AttemptRequest {}
}

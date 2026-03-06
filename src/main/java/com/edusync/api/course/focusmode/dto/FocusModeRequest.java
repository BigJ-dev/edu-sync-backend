package com.edusync.api.course.focusmode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public sealed interface FocusModeRequest {

    @Schema(name = "FocusModeActivate")
    record Activate(
            @NotNull(message = "Lecturer UUID is required")
            UUID lecturerUuid,

            @NotNull(message = "Course UUID is required")
            UUID courseUuid,

            UUID moduleUuid,

            @Size(max = 255, message = "Reason must not exceed 255 characters")
            String reason,

            Instant scheduledEnd
    ) implements FocusModeRequest {}

    @Schema(name = "FocusModeDeactivate")
    record Deactivate() implements FocusModeRequest {}
}

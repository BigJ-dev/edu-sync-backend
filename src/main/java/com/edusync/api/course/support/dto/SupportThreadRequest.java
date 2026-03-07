package com.edusync.api.course.support.dto;

import com.edusync.api.course.support.enums.ThreadPriority;
import com.edusync.api.course.support.enums.ThreadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

public sealed interface SupportThreadRequest {

    @Schema(name = "SupportThreadCreate")
    record Create(
            @NotNull(message = "Course UUID is required")
            UUID courseUuid,

            @NotNull(message = "Student UUID is required")
            UUID studentUuid,

            @NotBlank(message = "Subject is required")
            @Size(max = 255, message = "Subject must not exceed 255 characters")
            String subject,

            ThreadPriority priority,

            @NotBlank(message = "Body is required")
            String body
    ) implements SupportThreadRequest {}

    @Schema(name = "SupportThreadUpdateStatus")
    record UpdateStatus(
            @NotNull(message = "Status is required")
            ThreadStatus status
    ) implements SupportThreadRequest {}

    @Schema(name = "SupportThreadEscalate")
    record Escalate() implements SupportThreadRequest {}
}

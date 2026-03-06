package com.edusync.api.course.messaging.dto;

import com.edusync.api.course.messaging.enums.ThreadPriority;
import com.edusync.api.course.messaging.enums.ThreadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

public sealed interface ThreadRequest {

    @Schema(name = "ThreadCreate")
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
    ) implements ThreadRequest {}

    @Schema(name = "ThreadUpdateStatus")
    record UpdateStatus(
            @NotNull(message = "Status is required")
            ThreadStatus status
    ) implements ThreadRequest {}

    @Schema(name = "ThreadEscalate")
    record Escalate() implements ThreadRequest {}
}

package com.edusync.api.course.broadcast.dto;

import com.edusync.api.course.broadcast.enums.BroadcastPriority;
import com.edusync.api.course.broadcast.enums.BroadcastTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

public sealed interface BroadcastRequest {

    @Schema(name = "BroadcastCreate")
    record Create(
            UUID courseUuid,

            UUID moduleUuid,

            @NotNull(message = "Sender UUID is required")
            UUID sentByUuid,

            @NotBlank(message = "Broadcast title is required")
            @Size(max = 255, message = "Broadcast title must not exceed 255 characters")
            String title,

            @NotBlank(message = "Broadcast body is required")
            String body,

            @NotNull(message = "Target type is required")
            BroadcastTarget targetType,

            UUID targetSessionUuid,

            @Size(max = 500, message = "Attachment S3 key must not exceed 500 characters")
            String attachmentS3Key,

            @Size(max = 255, message = "Attachment name must not exceed 255 characters")
            String attachmentName,

            @NotNull(message = "Priority is required")
            BroadcastPriority priority,

            boolean sendEmail,

            List<UUID> studentUuids
    ) implements BroadcastRequest {}
}

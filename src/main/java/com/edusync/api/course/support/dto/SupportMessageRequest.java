package com.edusync.api.course.support.dto;

import com.edusync.api.course.support.enums.SenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

public sealed interface SupportMessageRequest {

    @Schema(name = "SupportMessageCreate")
    record Create(
            @NotNull(message = "Sender type is required")
            SenderType senderType,

            @NotNull(message = "Sender UUID is required")
            UUID senderUuid,

            @NotBlank(message = "Body is required")
            String body,

            @Size(max = 500, message = "Attachment S3 key must not exceed 500 characters")
            String attachmentS3Key,

            @Size(max = 255, message = "Attachment name must not exceed 255 characters")
            String attachmentName
    ) implements SupportMessageRequest {}
}

package com.edusync.api.notification.dto;

import com.edusync.api.notification.enums.NotificationType;
import com.edusync.api.notification.enums.RecipientType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public sealed interface NotificationRequest {

    @Schema(name = "NotificationCreate")
    record Create(
            @NotNull(message = "Recipient type is required")
            RecipientType recipientType,

            @NotNull(message = "Recipient ID is required")
            Long recipientId,

            @NotNull(message = "Notification type is required")
            NotificationType notificationType,

            @NotBlank(message = "Title is required")
            @Size(max = 255, message = "Title must not exceed 255 characters")
            String title,

            String body,

            UUID courseUuid,

            UUID moduleUuid,

            String entityType,

            Long entityId
    ) implements NotificationRequest {}

    @Schema(name = "NotificationMarkRead")
    record MarkRead() implements NotificationRequest {}

    @Schema(name = "NotificationDismiss")
    record Dismiss() implements NotificationRequest {}
}

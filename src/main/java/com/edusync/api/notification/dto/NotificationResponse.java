package com.edusync.api.notification.dto;

import com.edusync.api.notification.enums.NotificationType;
import com.edusync.api.notification.enums.RecipientType;
import com.edusync.api.notification.model.Notification;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID uuid,
        RecipientType recipientType,
        Long recipientId,
        NotificationType notificationType,
        String title,
        String body,
        UUID courseUuid,
        UUID moduleUuid,
        String entityType,
        Long entityId,
        Instant readAt,
        Instant dismissedAt,
        Instant createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getUuid(),
                notification.getRecipientType(),
                notification.getRecipientId(),
                notification.getNotificationType(),
                notification.getTitle(),
                notification.getBody(),
                notification.getCourse() != null ? notification.getCourse().getUuid() : null,
                notification.getModule() != null ? notification.getModule().getUuid() : null,
                notification.getEntityType(),
                notification.getEntityId(),
                notification.getReadAt(),
                notification.getDismissedAt(),
                notification.getCreatedAt()
        );
    }
}

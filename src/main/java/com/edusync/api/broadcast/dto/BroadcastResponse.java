package com.edusync.api.broadcast.dto;

import com.edusync.api.broadcast.enums.BroadcastPriority;
import com.edusync.api.broadcast.enums.BroadcastTarget;
import com.edusync.api.broadcast.model.BroadcastMessage;

import java.time.Instant;
import java.util.UUID;

public record BroadcastResponse(
        UUID uuid,
        UUID courseUuid,
        UUID moduleUuid,
        UUID sentByUuid,
        String title,
        String body,
        BroadcastTarget targetType,
        BroadcastPriority priority,
        boolean sendEmail,
        String attachmentS3Key,
        String attachmentName,
        Instant sentAt,
        Instant createdAt
) {
    public static BroadcastResponse from(BroadcastMessage message) {
        return new BroadcastResponse(
                message.getUuid(),
                message.getCourse() != null ? message.getCourse().getUuid() : null,
                message.getModule() != null ? message.getModule().getUuid() : null,
                message.getSentBy().getUuid(),
                message.getTitle(),
                message.getBody(),
                message.getTargetType(),
                message.getPriority(),
                message.isSendEmail(),
                message.getAttachmentS3Key(),
                message.getAttachmentName(),
                message.getSentAt(),
                message.getCreatedAt()
        );
    }
}

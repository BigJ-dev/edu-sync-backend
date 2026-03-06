package com.edusync.api.course.messaging.dto;

import com.edusync.api.course.messaging.enums.SenderType;
import com.edusync.api.course.messaging.model.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
        UUID uuid,
        UUID threadUuid,
        SenderType senderType,
        Long senderId,
        String body,
        String attachmentS3Key,
        String attachmentName,
        boolean isSystemMessage,
        Instant editedAt,
        Instant deletedAt,
        Instant createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getUuid(),
                message.getThread().getUuid(),
                message.getSenderType(),
                message.getSenderId(),
                message.getBody(),
                message.getAttachmentS3Key(),
                message.getAttachmentName(),
                message.isSystemMessage(),
                message.getEditedAt(),
                message.getDeletedAt(),
                message.getCreatedAt()
        );
    }
}

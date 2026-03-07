package com.edusync.api.course.support.dto;

import com.edusync.api.course.support.enums.SenderType;
import com.edusync.api.course.support.model.SupportMessage;

import java.time.Instant;
import java.util.UUID;

public record SupportMessageResponse(
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
    public static SupportMessageResponse from(SupportMessage message) {
        return new SupportMessageResponse(
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

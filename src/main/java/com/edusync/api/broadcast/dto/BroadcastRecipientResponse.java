package com.edusync.api.broadcast.dto;

import com.edusync.api.broadcast.model.BroadcastRecipient;

import java.time.Instant;
import java.util.UUID;

public record BroadcastRecipientResponse(
        UUID broadcastMessageUuid,
        UUID studentUuid,
        Instant readAt,
        boolean emailSent,
        Instant emailSentAt
) {
    public static BroadcastRecipientResponse from(BroadcastRecipient recipient) {
        return new BroadcastRecipientResponse(
                recipient.getBroadcastMessage().getUuid(),
                recipient.getStudent().getUuid(),
                recipient.getReadAt(),
                recipient.isEmailSent(),
                recipient.getEmailSentAt()
        );
    }
}

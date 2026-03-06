package com.edusync.api.course.messaging.dto;

import com.edusync.api.course.messaging.enums.ThreadPriority;
import com.edusync.api.course.messaging.enums.ThreadStatus;
import com.edusync.api.course.messaging.model.MessageThread;

import java.time.Instant;
import java.util.UUID;

public record ThreadResponse(
        UUID uuid,
        UUID courseUuid,
        UUID studentUuid,
        String subject,
        ThreadStatus status,
        ThreadPriority priority,
        boolean isEscalated,
        Instant escalatedAt,
        Instant lastMessageAt,
        int studentUnreadCount,
        int staffUnreadCount,
        Instant createdAt,
        Instant updatedAt
) {
    public static ThreadResponse from(MessageThread thread) {
        return new ThreadResponse(
                thread.getUuid(),
                thread.getCourse().getUuid(),
                thread.getStudent().getUuid(),
                thread.getSubject(),
                thread.getStatus(),
                thread.getPriority(),
                thread.isEscalated(),
                thread.getEscalatedAt(),
                thread.getLastMessageAt(),
                thread.getStudentUnreadCount(),
                thread.getStaffUnreadCount(),
                thread.getCreatedAt(),
                thread.getUpdatedAt()
        );
    }
}

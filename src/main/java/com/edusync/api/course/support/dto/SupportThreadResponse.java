package com.edusync.api.course.support.dto;

import com.edusync.api.course.support.enums.ThreadPriority;
import com.edusync.api.course.support.enums.ThreadStatus;
import com.edusync.api.course.support.model.SupportThread;

import java.time.Instant;
import java.util.UUID;

public record SupportThreadResponse(
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
    public static SupportThreadResponse from(SupportThread thread) {
        return new SupportThreadResponse(
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

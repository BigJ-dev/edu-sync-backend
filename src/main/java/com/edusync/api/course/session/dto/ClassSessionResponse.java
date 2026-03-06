package com.edusync.api.course.session.dto;

import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import com.edusync.api.course.session.model.ClassSession;

import java.time.Instant;
import java.util.UUID;

public record ClassSessionResponse(
        UUID uuid,
        UUID moduleUuid,
        UUID lecturerUuid,
        String title,
        String description,
        ClassSessionType sessionType,
        int sessionNumber,
        Instant scheduledStart,
        Instant scheduledEnd,
        Instant actualStart,
        Instant actualEnd,
        String teamsMeetingId,
        String teamsJoinUrl,
        String recordingS3Key,
        String venue,
        ClassSessionStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static ClassSessionResponse from(ClassSession session) {
        return new ClassSessionResponse(
                session.getUuid(),
                session.getModule().getUuid(),
                session.getLecturer().getUuid(),
                session.getTitle(),
                session.getDescription(),
                session.getSessionType(),
                session.getSessionNumber(),
                session.getScheduledStart(),
                session.getScheduledEnd(),
                session.getActualStart(),
                session.getActualEnd(),
                session.getTeamsMeetingId(),
                session.getTeamsJoinUrl(),
                session.getRecordingS3Key(),
                session.getVenue(),
                session.getStatus(),
                session.getCreatedAt(),
                session.getUpdatedAt()
        );
    }
}

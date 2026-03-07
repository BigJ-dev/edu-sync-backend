package com.edusync.api.timetable.dto;

import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import com.edusync.api.course.session.model.ClassSession;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TimetableEntry(
        UUID sessionUuid,
        String sessionTitle,
        ClassSessionType sessionType,
        int sessionNumber,
        ClassSessionStatus status,
        Instant scheduledStart,
        Instant scheduledEnd,
        String venue,
        String teamsJoinUrl,
        UUID courseUuid,
        String courseCode,
        String courseTitle,
        UUID moduleUuid,
        String moduleTitle,
        UUID lecturerUuid,
        UUID groupUuid
) {
    public static TimetableEntry from(ClassSession session) {
        var course = session.getModule().getCourse();
        return new TimetableEntry(
                session.getUuid(),
                session.getTitle(),
                session.getSessionType(),
                session.getSessionNumber(),
                session.getStatus(),
                session.getScheduledStart(),
                session.getScheduledEnd(),
                session.getVenue(),
                session.getTeamsJoinUrl(),
                course.getUuid(),
                course.getCode(),
                course.getTitle(),
                session.getModule().getUuid(),
                session.getModule().getTitle(),
                session.getLecturer().getUuid(),
                Objects.nonNull(session.getGroup()) ? session.getGroup().getUuid() : null
        );
    }
}

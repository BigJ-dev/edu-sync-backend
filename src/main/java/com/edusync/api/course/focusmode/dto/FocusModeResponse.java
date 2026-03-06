package com.edusync.api.course.focusmode.dto;

import com.edusync.api.course.focusmode.model.LecturerFocusMode;

import java.time.Instant;
import java.util.UUID;

public record FocusModeResponse(
        UUID lecturerUuid,
        UUID courseUuid,
        UUID moduleUuid,
        boolean isActive,
        String reason,
        Instant activatedAt,
        Instant scheduledEnd,
        Instant updatedAt
) {
    public static FocusModeResponse from(LecturerFocusMode focusMode) {
        return new FocusModeResponse(
                focusMode.getLecturer().getUuid(),
                focusMode.getCourse().getUuid(),
                focusMode.getModule() != null ? focusMode.getModule().getUuid() : null,
                focusMode.isActive(),
                focusMode.getReason(),
                focusMode.getActivatedAt(),
                focusMode.getScheduledEnd(),
                focusMode.getUpdatedAt()
        );
    }
}

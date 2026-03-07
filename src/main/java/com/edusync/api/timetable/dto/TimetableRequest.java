package com.edusync.api.timetable.dto;

import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.UUID;

public sealed interface TimetableRequest {

    @Schema(name = "TimetableSessionCreate")
    record CreateSession(
            @NotNull(message = "Module UUID is required")
            UUID moduleUuid,

            @NotNull(message = "Lecturer UUID is required")
            UUID lecturerUuid,

            @NotBlank(message = "Title is required")
            @Size(max = 255, message = "Title must not exceed 255 characters")
            String title,

            @Size(max = 5000, message = "Description must not exceed 5000 characters")
            String description,

            @NotNull(message = "Session type is required")
            ClassSessionType sessionType,

            @NotNull(message = "Session number is required")
            @Min(value = 1, message = "Session number must be at least 1")
            Integer sessionNumber,

            @NotNull(message = "Scheduled start is required")
            Instant scheduledStart,

            @NotNull(message = "Scheduled end is required")
            Instant scheduledEnd,

            UUID groupUuid,

            String teamsMeetingId,
            String teamsJoinUrl,

            @Size(max = 500, message = "Venue must not exceed 500 characters")
            String venue
    ) implements TimetableRequest {}

    @Schema(name = "TimetableSessionUpdate")
    record UpdateSession(
            @NotBlank(message = "Title is required")
            @Size(max = 255, message = "Title must not exceed 255 characters")
            String title,

            @Size(max = 5000, message = "Description must not exceed 5000 characters")
            String description,

            @NotNull(message = "Session number is required")
            @Min(value = 1, message = "Session number must be at least 1")
            Integer sessionNumber,

            @NotNull(message = "Scheduled start is required")
            Instant scheduledStart,

            @NotNull(message = "Scheduled end is required")
            Instant scheduledEnd,

            UUID groupUuid,

            String teamsMeetingId,
            String teamsJoinUrl,

            @Size(max = 500, message = "Venue must not exceed 500 characters")
            String venue
    ) implements TimetableRequest {}

    @Schema(name = "TimetableSessionUpdateStatus")
    record UpdateStatus(
            @NotNull(message = "Status is required")
            ClassSessionStatus status
    ) implements TimetableRequest {}
}

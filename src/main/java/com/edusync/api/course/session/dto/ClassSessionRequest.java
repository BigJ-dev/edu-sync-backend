package com.edusync.api.course.session.dto;

import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.UUID;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface ClassSessionRequest {

    @Schema(name = "ClassSessionCreate")
    record Create(
            @NotBlank(message = _SESSION_TITLE_REQUIRED)
            @Size(max = 255, message = _SESSION_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _SESSION_DESCRIPTION_SIZE)
            String description,

            @NotNull(message = _SESSION_TYPE_REQUIRED)
            ClassSessionType sessionType,

            @NotNull(message = _SESSION_NUMBER_REQUIRED)
            @Min(value = 1, message = _SESSION_NUMBER_MIN)
            Integer sessionNumber,

            @NotNull(message = _SESSION_SCHEDULED_START_REQUIRED)
            Instant scheduledStart,

            @NotNull(message = _SESSION_SCHEDULED_END_REQUIRED)
            Instant scheduledEnd,

            @NotNull(message = _SESSION_LECTURER_REQUIRED)
            UUID lecturerUuid,

            String teamsMeetingId,
            String teamsJoinUrl,

            @Size(max = 500, message = _SESSION_VENUE_SIZE)
            String venue
    ) implements ClassSessionRequest {}

    @Schema(name = "ClassSessionUpdate")
    record Update(
            @NotBlank(message = _SESSION_TITLE_REQUIRED)
            @Size(max = 255, message = _SESSION_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _SESSION_DESCRIPTION_SIZE)
            String description,

            @NotNull(message = _SESSION_NUMBER_REQUIRED)
            @Min(value = 1, message = _SESSION_NUMBER_MIN)
            Integer sessionNumber,

            @NotNull(message = _SESSION_SCHEDULED_START_REQUIRED)
            Instant scheduledStart,

            @NotNull(message = _SESSION_SCHEDULED_END_REQUIRED)
            Instant scheduledEnd,

            String teamsMeetingId,
            String teamsJoinUrl,

            @Size(max = 500, message = _SESSION_VENUE_SIZE)
            String venue
    ) implements ClassSessionRequest {}

    @Schema(name = "ClassSessionUpdateStatus")
    record UpdateStatus(
            @NotNull(message = _SESSION_STATUS_REQUIRED)
            ClassSessionStatus status
    ) implements ClassSessionRequest {}
}

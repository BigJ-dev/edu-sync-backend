package com.edusync.api.course.attendance.dto;

import com.edusync.api.course.attendance.enums.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface AttendanceRequest {

    @Schema(name = "AttendanceRecord")
    record Record(
            @NotNull(message = _ATTENDANCE_STUDENT_REQUIRED)
            UUID studentUuid,

            @NotNull(message = _ATTENDANCE_STATUS_REQUIRED)
            AttendanceStatus attendanceStatus,

            Instant joinTime,
            Instant leaveTime,
            Integer totalDurationMinutes
    ) implements AttendanceRequest {}

    @Schema(name = "AttendanceOverride")
    record Override(
            @NotNull(message = _ATTENDANCE_STATUS_REQUIRED)
            AttendanceStatus attendanceStatus,

            @NotNull(message = _ATTENDANCE_OVERRIDE_REASON_REQUIRED)
            UUID overrideByUuid,

            @NotBlank(message = _ATTENDANCE_OVERRIDE_REASON_REQUIRED)
            @Size(max = 500, message = _ATTENDANCE_OVERRIDE_REASON_SIZE)
            String overrideReason
    ) implements AttendanceRequest {}
}

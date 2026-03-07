package com.edusync.api.teams.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public sealed interface TeamsRequest {

    @Schema(name = "TeamsInviteGuest")
    record InviteGuest(
            @NotNull(message = "Student UUID is required")
            UUID studentUuid
    ) implements TeamsRequest {}

    @Schema(name = "TeamsCreateMeeting")
    record CreateMeeting(
            @NotNull(message = "Class session UUID is required")
            UUID classSessionUuid
    ) implements TeamsRequest {}

    @Schema(name = "TeamsSyncAttendance")
    record SyncAttendance(
            @NotNull(message = "Class session UUID is required")
            UUID classSessionUuid
    ) implements TeamsRequest {}

    @Schema(name = "TeamsBulkInvite")
    record BulkInvite(
            @NotNull(message = "Class session UUID is required")
            UUID classSessionUuid
    ) implements TeamsRequest {}

    @Schema(name = "TeamsMonthlyReport")
    record MonthlyReport(
            @NotNull(message = "Course UUID is required")
            UUID courseUuid,

            @NotNull(message = "Year is required")
            Integer year,

            @NotNull(message = "Month is required (1-12)")
            Integer month
    ) implements TeamsRequest {}
}

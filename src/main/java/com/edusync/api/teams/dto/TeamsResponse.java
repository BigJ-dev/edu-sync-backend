package com.edusync.api.teams.dto;

import com.edusync.api.course.session.enums.ClassSessionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public sealed interface TeamsResponse {

    record GuestInvite(
            UUID studentUuid,
            String email,
            String azureAdGuestId,
            String inviteRedeemUrl
    ) implements TeamsResponse {}

    record MeetingCreated(
            UUID classSessionUuid,
            String teamsMeetingId,
            String teamsJoinUrl,
            UUID lecturerUuid,
            String lecturerName
    ) implements TeamsResponse {}

    record AttendanceSynced(
            UUID classSessionUuid,
            int recordsSynced
    ) implements TeamsResponse {}

    record BulkInviteResult(
            UUID classSessionUuid,
            int totalStudents,
            int newInvitesSent,
            int alreadyInvited
    ) implements TeamsResponse {}

    record SessionStudent(
            UUID studentUuid,
            String studentNumber,
            String firstName,
            String lastName,
            String email,
            boolean azureAdInvited
    ) {}

    record MonthlyReportResult(
            UUID courseUuid,
            int year,
            int month,
            List<StudentAttendanceSummary> studentSummaries,
            List<ClassAttendanceSummary> classSummaries
    ) implements TeamsResponse {}

    record StudentAttendanceSummary(
            UUID studentUuid,
            String studentNumber,
            String firstName,
            String lastName,
            int totalClassesScheduled,
            int totalClassesAttended,
            BigDecimal attendancePercentage,
            int totalMinutesAttended
    ) {}

    record ClassAttendanceSummary(
            UUID classSessionUuid,
            String title,
            Instant scheduledStart,
            ClassSessionType sessionType,
            boolean isOnlineClass,
            UUID lecturerUuid,
            String lecturerFirstName,
            String lecturerLastName,
            int totalEnrolled,
            int totalAttended,
            BigDecimal attendancePercentage,
            int averageDurationMinutes
    ) {}
}

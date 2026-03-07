package com.edusync.api.teams.util;

import com.edusync.api.course.attendance.enums.AttendanceEvent;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.model.AttendanceLog;
import com.edusync.api.course.attendance.model.AttendanceRecord;
import com.edusync.api.course.session.model.ClassSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeamsAttendanceCalculator {

    private static final double PRESENT_THRESHOLD = 0.75;
    private static final double PARTIAL_THRESHOLD = 0.25;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int NO_ATTENDANCE_SECONDS = 0;

    public static int extractAttendanceDurationMinutes(com.microsoft.graph.models.AttendanceRecord graphRecord) {
        int totalSeconds = Objects.nonNull(graphRecord.getTotalAttendanceInSeconds()) ? graphRecord.getTotalAttendanceInSeconds() : NO_ATTENDANCE_SECONDS;
        return totalSeconds / SECONDS_PER_MINUTE;
    }

    public static AttendanceStatus calculateAttendanceStatusFromDuration(int durationMinutes, ClassSession session) {
        long scheduledMinutes = Duration.between(session.getScheduledStart(), session.getScheduledEnd()).toMinutes();
        if (scheduledMinutes <= 0) return AttendanceStatus.PRESENT;

        double ratio = (double) durationMinutes / scheduledMinutes;
        if (ratio >= PRESENT_THRESHOLD) return AttendanceStatus.PRESENT;
        if (ratio >= PARTIAL_THRESHOLD) return AttendanceStatus.PARTIAL;
        return AttendanceStatus.ABSENT;
    }

    public static AttendanceLog buildTeamsAttendanceLog(AttendanceRecord record, AttendanceEvent eventType, Instant eventTime, String email) {
        return AttendanceLog.builder()
                .attendanceRecord(record).eventType(eventType).eventTime(eventTime).teamsIdentityEmail(email).build();
    }
}

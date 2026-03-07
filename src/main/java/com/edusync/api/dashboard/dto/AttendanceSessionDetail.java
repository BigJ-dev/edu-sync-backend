package com.edusync.api.dashboard.dto;

import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.model.AttendanceRecord;

import java.time.Instant;
import java.util.UUID;

public record AttendanceSessionDetail(
        UUID sessionUuid,
        String sessionTitle,
        Instant scheduledStart,
        Instant scheduledEnd,
        AttendanceStatus attendanceStatus,
        Instant joinTime,
        Instant leaveTime,
        Integer durationMinutes,
        boolean syncedFromTeams
) {
    public static AttendanceSessionDetail from(AttendanceRecord record) {
        return new AttendanceSessionDetail(
                record.getClassSession().getUuid(),
                record.getClassSession().getTitle(),
                record.getClassSession().getScheduledStart(),
                record.getClassSession().getScheduledEnd(),
                record.getAttendanceStatus(),
                record.getJoinTime(),
                record.getLeaveTime(),
                record.getTotalDurationMinutes(),
                record.isSyncedFromTeams()
        );
    }
}

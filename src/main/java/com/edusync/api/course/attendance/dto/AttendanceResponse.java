package com.edusync.api.course.attendance.dto;

import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.model.AttendanceRecord;

import java.time.Instant;
import java.util.UUID;

public record AttendanceResponse(
        Long id,
        UUID classSessionUuid,
        UUID studentUuid,
        UUID courseUuid,
        UUID moduleUuid,
        Instant joinTime,
        Instant leaveTime,
        Integer totalDurationMinutes,
        AttendanceStatus attendanceStatus,
        boolean syncedFromTeams,
        boolean manuallyOverridden,
        UUID overrideByUuid,
        String overrideReason,
        Instant createdAt,
        Instant updatedAt
) {
    public static AttendanceResponse from(AttendanceRecord record) {
        return new AttendanceResponse(
                record.getId(),
                record.getClassSession().getUuid(),
                record.getStudent().getUuid(),
                record.getCourse().getUuid(),
                record.getModule().getUuid(),
                record.getJoinTime(),
                record.getLeaveTime(),
                record.getTotalDurationMinutes(),
                record.getAttendanceStatus(),
                record.isSyncedFromTeams(),
                record.isManuallyOverridden(),
                record.getOverrideBy() != null ? record.getOverrideBy().getUuid() : null,
                record.getOverrideReason(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}

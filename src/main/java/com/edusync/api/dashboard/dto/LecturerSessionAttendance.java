package com.edusync.api.dashboard.dto;

import com.edusync.api.course.attendance.enums.AttendanceStatus;

import java.time.Instant;
import java.util.UUID;

public record LecturerSessionAttendance(
        UUID studentUuid,
        String studentNumber,
        String firstName,
        String lastName,
        AttendanceStatus attendanceStatus,
        Instant joinTime,
        Instant leaveTime,
        Integer durationMinutes,
        boolean syncedFromTeams
) {}

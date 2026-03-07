package com.edusync.api.dashboard.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SessionAttendanceOverview(
        UUID sessionUuid,
        String sessionTitle,
        String courseCode,
        String courseTitle,
        Instant scheduledStart,
        Instant scheduledEnd,
        long totalStudents,
        long presentCount,
        long partialCount,
        long absentCount,
        BigDecimal attendancePct,
        List<LecturerSessionAttendance> students
) {}

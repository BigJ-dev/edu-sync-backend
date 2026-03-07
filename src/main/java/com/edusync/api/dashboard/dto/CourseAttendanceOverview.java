package com.edusync.api.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CourseAttendanceOverview(
        UUID courseUuid,
        String courseCode,
        String courseTitle,
        long totalSessions,
        long presentCount,
        long partialCount,
        long absentCount,
        BigDecimal attendancePct,
        Integer totalDurationMinutes,
        List<AttendanceSessionDetail> sessions
) {}

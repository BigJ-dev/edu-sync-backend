package com.edusync.api.dashboard.dto;

import java.util.List;
import java.util.UUID;

public record LecturerDashboard(
        UUID lecturerUuid,
        long totalCourses,
        long totalStudentsAcrossCourses,
        long upcomingSessionsThisWeek,
        List<LecturerCourseSummary> courses
) {}

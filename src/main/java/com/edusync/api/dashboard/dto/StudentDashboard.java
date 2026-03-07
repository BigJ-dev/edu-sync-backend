package com.edusync.api.dashboard.dto;

import java.util.List;
import java.util.UUID;

public record StudentDashboard(
        UUID studentUuid,
        String studentNumber,
        long enrolledCourses,
        long upcomingSessionsThisWeek,
        List<StudentCourseSummary> courses
) {}

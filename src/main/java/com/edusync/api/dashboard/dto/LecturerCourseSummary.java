package com.edusync.api.dashboard.dto;

import java.util.UUID;

public record LecturerCourseSummary(
        UUID courseUuid,
        String courseCode,
        String courseTitle,
        long enrolledStudents,
        long pendingSubmissions
) {}

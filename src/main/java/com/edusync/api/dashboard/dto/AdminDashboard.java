package com.edusync.api.dashboard.dto;

public record AdminDashboard(
        long totalStudents,
        long activeStudents,
        long totalCourses,
        long totalLecturers,
        long pendingApplications,
        long approvedApplications,
        long rejectedApplications,
        long totalEnrollments
) {}

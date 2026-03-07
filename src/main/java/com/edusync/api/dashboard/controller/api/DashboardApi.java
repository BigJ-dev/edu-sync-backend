package com.edusync.api.dashboard.controller.api;

import com.edusync.api.dashboard.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "Dashboard", description = "Endpoints for viewing dashboard analytics. Provides platform-wide stats for admins, " +
        "course-level insights for lecturers, and academic progress summaries for students.")
@RequestMapping("/dashboard")
public interface DashboardApi {

    @GetMapping("/admin")
    @Operation(
            summary = "Get admin dashboard",
            description = "Returns platform-wide statistics including total students, courses, lecturers, " +
                    "application counts by status, and total enrollments."
    )
    @ApiResponse(responseCode = "200", description = "Dashboard retrieved successfully")
    AdminDashboard getAdminDashboard();

    @GetMapping("/lecturer/{lecturerUuid}")
    @Operation(
            summary = "Get lecturer dashboard",
            description = "Returns a lecturer's course overview including student counts, pending submissions, " +
                    "and upcoming sessions for the current week."
    )
    @ApiResponse(responseCode = "200", description = "Dashboard retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    LecturerDashboard getLecturerDashboard(@PathVariable UUID lecturerUuid);

    @GetMapping("/student/{studentUuid}")
    @Operation(
            summary = "Get student dashboard",
            description = "Returns a student's academic overview including enrolled courses, attendance percentages, " +
                    "average grades, pending assessments, and upcoming sessions for the current week."
    )
    @ApiResponse(responseCode = "200", description = "Dashboard retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    StudentDashboard getStudentDashboard(@PathVariable UUID studentUuid);

    @GetMapping("/student/{studentUuid}/attendance")
    @Operation(
            summary = "Get student attendance details",
            description = "Returns detailed attendance data per course for a student, including per-session " +
                    "join/leave times, duration in minutes, Teams sync status, and overall attendance percentage."
    )
    @ApiResponse(responseCode = "200", description = "Attendance retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    List<CourseAttendanceOverview> getStudentAttendance(@PathVariable UUID studentUuid);

    @GetMapping("/session/{sessionUuid}/attendance")
    @Operation(
            summary = "Get session attendance overview",
            description = "Returns attendance data for a specific class session, showing each student's " +
                    "attendance status, join/leave times, duration, and whether it was synced from Teams."
    )
    @ApiResponse(responseCode = "200", description = "Session attendance retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Class session not found")
    SessionAttendanceOverview getSessionAttendance(@PathVariable UUID sessionUuid);
}

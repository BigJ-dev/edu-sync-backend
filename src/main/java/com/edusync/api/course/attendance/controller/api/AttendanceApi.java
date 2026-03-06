package com.edusync.api.course.attendance.controller.api;

import com.edusync.api.course.attendance.dto.AttendanceRequest;
import com.edusync.api.course.attendance.dto.AttendanceResponse;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Attendance", description = "Endpoints for managing attendance records. Attendance is tracked per student per session, with support for manual recording, Teams sync, and lecturer overrides.")
@RequestMapping("/class-sessions/{classSessionUuid}/attendance")
public interface AttendanceApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Record attendance for a student",
            description = "Records attendance for a student in a class session. " +
                    "Used for manual attendance recording by lecturers (physical sessions) " +
                    "or programmatic recording from Teams sync."
    )
    @ApiResponse(responseCode = "201", description = "Attendance recorded successfully")
    @ApiResponse(responseCode = "404", description = "Session or student not found")
    @ApiResponse(responseCode = "409", description = "Attendance already recorded for this student in this session")
    AttendanceResponse record(@PathVariable UUID classSessionUuid, @Valid @RequestBody AttendanceRequest.Record request);

    @GetMapping
    @Operation(
            summary = "List attendance for a session",
            description = "Returns all attendance records for a session. " +
                    "Supports filtering by attendance status."
    )
    @ApiResponse(responseCode = "200", description = "Attendance records retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    List<AttendanceResponse> findAllBySession(
            @PathVariable UUID classSessionUuid,
            @RequestParam(required = false) AttendanceStatus status);

    @PatchMapping("/students/{studentUuid}/override")
    @Operation(
            summary = "Override attendance for a student",
            description = "Overrides a student's attendance status with a reason and audit trail. " +
                    "Used when a lecturer needs to correct auto-synced attendance or mark exceptions."
    )
    @ApiResponse(responseCode = "200", description = "Attendance overridden successfully")
    @ApiResponse(responseCode = "404", description = "Attendance record not found")
    AttendanceResponse override(
            @PathVariable UUID classSessionUuid,
            @PathVariable UUID studentUuid,
            @Valid @RequestBody AttendanceRequest.Override request);
}

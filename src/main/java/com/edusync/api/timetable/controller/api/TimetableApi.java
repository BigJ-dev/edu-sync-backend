package com.edusync.api.timetable.controller.api;

import com.edusync.api.timetable.dto.TimetableEntry;
import com.edusync.api.timetable.dto.TimetableRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Timetable", description = "Endpoints for viewing and managing class schedules. " +
        "Students see sessions for their enrolled courses, lecturers see and manage sessions they are teaching.")
@RequestMapping("/timetable")
public interface TimetableApi {

    @GetMapping("/student/{studentUuid}")
    @Operation(
            summary = "Get student timetable",
            description = "Returns all class sessions for a student's enrolled courses within the specified date range. Defaults to the current week if no dates are provided."
    )
    @ApiResponse(responseCode = "200", description = "Timetable retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    List<TimetableEntry> getStudentTimetable(
            @PathVariable UUID studentUuid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to);

    @GetMapping("/lecturer/{lecturerUuid}")
    @Operation(
            summary = "Get lecturer timetable",
            description = "Returns all class sessions a lecturer is teaching within the specified date range. Defaults to the current week if no dates are provided."
    )
    @ApiResponse(responseCode = "200", description = "Timetable retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    List<TimetableEntry> getLecturerTimetable(
            @PathVariable UUID lecturerUuid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to);

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a timetable session",
            description = "Creates a new class session for a module. The session will appear on both the lecturer's " +
                    "and enrolled students' timetables."
    )
    @ApiResponse(responseCode = "201", description = "Session created successfully")
    @ApiResponse(responseCode = "404", description = "Module or lecturer not found")
    @ApiResponse(responseCode = "409", description = "Session number already taken in this module")
    TimetableEntry createSession(@Valid @RequestBody TimetableRequest.CreateSession request);

    @PutMapping("/sessions/{sessionUuid}")
    @Operation(
            summary = "Update a timetable session",
            description = "Updates an existing class session's details such as title, schedule, venue, or Teams link. " +
                    "Changes reflect on all timetables."
    )
    @ApiResponse(responseCode = "200", description = "Session updated successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @ApiResponse(responseCode = "409", description = "Session number already taken in this module")
    TimetableEntry updateSession(@PathVariable UUID sessionUuid, @Valid @RequestBody TimetableRequest.UpdateSession request);

    @PatchMapping("/sessions/{sessionUuid}/status")
    @Operation(
            summary = "Update session status",
            description = "Changes the lifecycle status of a session (SCHEDULED, LIVE, COMPLETED, CANCELLED). " +
                    "Going LIVE auto-sets actual start time. COMPLETED auto-sets actual end time."
    )
    @ApiResponse(responseCode = "200", description = "Session status updated successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    TimetableEntry updateSessionStatus(@PathVariable UUID sessionUuid, @Valid @RequestBody TimetableRequest.UpdateStatus request);
}

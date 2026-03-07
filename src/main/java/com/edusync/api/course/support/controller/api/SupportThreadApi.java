package com.edusync.api.course.support.controller.api;

import com.edusync.api.course.support.dto.SupportThreadRequest;
import com.edusync.api.course.support.dto.SupportThreadResponse;
import com.edusync.api.course.support.enums.ThreadPriority;
import com.edusync.api.course.support.enums.ThreadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Support Threads", description = "Endpoints for managing course support threads. Students open threads to get help from lecturers/admins within a course.")
@RequestMapping("/courses/{courseUuid}/support-threads")
public interface SupportThreadApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new support thread",
            description = "Creates a new support thread within a course. An initial message is created from the student. " +
                    "The thread starts in OPEN status with staff_unread_count set to 1."
    )
    @ApiResponse(responseCode = "201", description = "Thread created successfully")
    @ApiResponse(responseCode = "404", description = "Course or student not found")
    SupportThreadResponse create(@PathVariable UUID courseUuid, @Valid @RequestBody SupportThreadRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all support threads for a course",
            description = "Returns all support threads belonging to a course. Supports filtering by status, " +
                    "priority, escalation flag, and searching by subject."
    )
    @ApiResponse(responseCode = "200", description = "Threads retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    List<SupportThreadResponse> findAll(
            @PathVariable UUID courseUuid,
            @RequestParam(required = false) ThreadStatus status,
            @RequestParam(required = false) ThreadPriority priority,
            @RequestParam(required = false) Boolean escalated,
            @RequestParam(required = false) String search);

    @GetMapping("/{threadUuid}")
    @Operation(
            summary = "Get support thread by UUID",
            description = "Retrieves a single support thread's details by its unique identifier."
    )
    @ApiResponse(responseCode = "200", description = "Thread retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Thread not found")
    SupportThreadResponse findByUuid(@PathVariable UUID courseUuid, @PathVariable UUID threadUuid);

    @PatchMapping("/{threadUuid}/status")
    @Operation(
            summary = "Update thread status",
            description = "Changes the status of a support thread (e.g. OPEN to RESOLVED or CLOSED)."
    )
    @ApiResponse(responseCode = "200", description = "Thread status updated successfully")
    @ApiResponse(responseCode = "404", description = "Thread not found")
    SupportThreadResponse updateStatus(@PathVariable UUID courseUuid, @PathVariable UUID threadUuid, @Valid @RequestBody SupportThreadRequest.UpdateStatus request);

    @PostMapping("/{threadUuid}/escalate")
    @Operation(
            summary = "Escalate a thread",
            description = "Marks a thread as escalated. Requires the UUID of the user performing the escalation."
    )
    @ApiResponse(responseCode = "200", description = "Thread escalated successfully")
    @ApiResponse(responseCode = "404", description = "Thread or user not found")
    SupportThreadResponse escalate(@PathVariable UUID courseUuid, @PathVariable UUID threadUuid, @RequestParam UUID escalatedByUuid);
}

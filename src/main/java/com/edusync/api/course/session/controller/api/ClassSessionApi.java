package com.edusync.api.course.session.controller.api;

import com.edusync.api.course.session.dto.ClassSessionRequest;
import com.edusync.api.course.session.dto.ClassSessionResponse;
import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Class Sessions", description = "Endpoints for managing class sessions. Sessions represent scheduled teaching events (online via Teams or physical in-person) within a module.")
@RequestMapping("/modules/{moduleUuid}/class-sessions")
public interface ClassSessionApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new class session",
            description = "Creates a new class session within a module in SCHEDULED status. " +
                    "Supports both ONLINE (Teams) and PHYSICAL (in-person) session types."
    )
    @ApiResponse(responseCode = "201", description = "Class session created successfully")
    @ApiResponse(responseCode = "404", description = "Module or lecturer not found")
    @ApiResponse(responseCode = "409", description = "Session number already taken in this module")
    ClassSessionResponse create(@PathVariable UUID moduleUuid, @Valid @RequestBody ClassSessionRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all class sessions for a module",
            description = "Returns all class sessions belonging to a module. Supports filtering by status, " +
                    "session type, and searching by title."
    )
    @ApiResponse(responseCode = "200", description = "Class sessions retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Module not found")
    List<ClassSessionResponse> findAllByModule(
            @PathVariable UUID moduleUuid,
            @RequestParam(required = false) ClassSessionStatus status,
            @RequestParam(required = false) ClassSessionType sessionType,
            @RequestParam(required = false) String search);

    @GetMapping("/{classSessionUuid}")
    @Operation(
            summary = "Get class session by UUID",
            description = "Retrieves a single class session's details by its unique identifier. " +
                    "Used to view session information, schedule, and Teams meeting details."
    )
    @ApiResponse(responseCode = "200", description = "Class session retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Class session not found")
    ClassSessionResponse findByUuid(@PathVariable UUID moduleUuid, @PathVariable UUID classSessionUuid);

    @PutMapping("/{classSessionUuid}")
    @Operation(
            summary = "Update class session details",
            description = "Updates a class session's information such as title, schedule, venue, or Teams details. " +
                    "Used when session details need to be corrected or rescheduled."
    )
    @ApiResponse(responseCode = "200", description = "Class session updated successfully")
    @ApiResponse(responseCode = "404", description = "Class session not found")
    @ApiResponse(responseCode = "409", description = "Session number already taken in this module")
    ClassSessionResponse update(@PathVariable UUID moduleUuid, @PathVariable UUID classSessionUuid, @Valid @RequestBody ClassSessionRequest.Update request);

    @PatchMapping("/{classSessionUuid}/status")
    @Operation(
            summary = "Update class session status",
            description = "Changes the lifecycle status of a class session (e.g. SCHEDULED to LIVE, or LIVE to COMPLETED). " +
                    "Going LIVE auto-sets actual start time. COMPLETED auto-sets actual end time."
    )
    @ApiResponse(responseCode = "200", description = "Class session status updated successfully")
    @ApiResponse(responseCode = "404", description = "Class session not found")
    ClassSessionResponse updateStatus(@PathVariable UUID moduleUuid, @PathVariable UUID classSessionUuid, @Valid @RequestBody ClassSessionRequest.UpdateStatus request);
}

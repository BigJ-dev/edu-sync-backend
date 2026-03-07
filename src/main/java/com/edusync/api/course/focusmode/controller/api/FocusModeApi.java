package com.edusync.api.course.focusmode.controller.api;

import com.edusync.api.course.focusmode.dto.FocusModeRequest;
import com.edusync.api.course.focusmode.dto.FocusModeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Focus Mode", description = "Endpoints for managing lecturer focus mode. Focus mode allows lecturers to signal focused teaching periods on a course or module level.")
@RequestMapping("/focus-mode")
public interface FocusModeApi {

    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Activate focus mode",
            description = "Activates focus mode for a lecturer on a specific course, optionally scoped to a module. " +
                    "Creates the record if it does not exist, or updates an existing one."
    )
    @ApiResponse(responseCode = "200", description = "Focus mode activated successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer, course, or module not found")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    FocusModeResponse activateFocusMode(@Valid @RequestBody FocusModeRequest.Activate request);

    @PostMapping("/deactivate")
    @Operation(
            summary = "Deactivate focus mode",
            description = "Deactivates focus mode for a lecturer on a specific course. " +
                    "Clears activation timestamp and scheduled end."
    )
    @ApiResponse(responseCode = "200", description = "Focus mode deactivated successfully")
    @ApiResponse(responseCode = "404", description = "Focus mode record not found")
    FocusModeResponse deactivateFocusMode(
            @RequestParam UUID lecturerUuid,
            @RequestParam UUID courseUuid,
            @RequestParam(required = false) UUID moduleUuid);

    @GetMapping("/lecturer/{lecturerUuid}")
    @Operation(
            summary = "List focus modes for a lecturer",
            description = "Returns all focus mode records for a given lecturer across all courses and modules."
    )
    @ApiResponse(responseCode = "200", description = "Focus modes retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    List<FocusModeResponse> findFocusModesByLecturer(@PathVariable UUID lecturerUuid);

    @GetMapping("/course/{courseUuid}")
    @Operation(
            summary = "List active focus modes for a course",
            description = "Returns all currently active focus mode records for a given course."
    )
    @ApiResponse(responseCode = "200", description = "Active focus modes retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    List<FocusModeResponse> findActiveFocusModesByCourse(@PathVariable UUID courseUuid);

    @GetMapping("/check")
    @Operation(
            summary = "Check if focus mode is active",
            description = "Returns whether focus mode is currently active for a lecturer on a specific course, " +
                    "optionally scoped to a module."
    )
    @ApiResponse(responseCode = "200", description = "Focus mode status retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer, course, or module not found")
    boolean isFocusModeActive(
            @RequestParam UUID lecturerUuid,
            @RequestParam UUID courseUuid,
            @RequestParam(required = false) UUID moduleUuid);
}

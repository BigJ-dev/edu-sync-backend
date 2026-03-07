package com.edusync.api.actor.lecturer.controller.api;

import com.edusync.api.actor.common.dto.AppUserRequest;
import com.edusync.api.actor.common.dto.AppUserResponse;
import com.edusync.api.actor.student.dto.StudentRequest;
import com.edusync.api.actor.student.dto.StudentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Lecturers", description = "Endpoints for managing lecturers. Lecturers deliver courses, manage learning content, and interact with students on the EduSync platform.")
@RequestMapping("/lecturers")
public interface LecturerApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register a new lecturer",
            description = "Creates a new lecturer account linked to an existing AWS Cognito identity. " +
                    "Used during onboarding after the lecturer has been registered in Cognito."
    )
    @ApiResponse(responseCode = "201", description = "Lecturer created successfully")
    @ApiResponse(responseCode = "409", description = "Email or Cognito identity already exists")
    AppUserResponse createLecturer(@Valid @RequestBody AppUserRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all lecturers",
            description = "Returns a paginated list of lecturers. Supports filtering by active status " +
                    "and searching by first name, last name, or email."
    )
    @ApiResponse(responseCode = "200", description = "Lecturers retrieved successfully")
    Page<AppUserResponse> findAllLecturers(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String search,
            Pageable pageable);

    @GetMapping("/{uuid}")
    @Operation(
            summary = "Get lecturer by UUID",
            description = "Retrieves a single lecturer's details by their unique identifier. " +
                    "Used to view a lecturer's profile information."
    )
    @ApiResponse(responseCode = "200", description = "Lecturer retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    AppUserResponse findLecturerByUuid(@PathVariable UUID uuid);

    @PutMapping("/{uuid}")
    @Operation(
            summary = "Update lecturer details",
            description = "Updates a lecturer's profile information such as name and email. " +
                    "Used when a lecturer's personal details need to be corrected or changed."
    )
    @ApiResponse(responseCode = "200", description = "Lecturer updated successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    AppUserResponse updateLecturer(@PathVariable UUID uuid, @Valid @RequestBody AppUserRequest.Update request);

    @GetMapping("/by-cognito-sub/{cognitoSub}")
    @Operation(
            summary = "Lookup lecturer by Cognito subject",
            description = "Finds a lecturer by their AWS Cognito subject identifier. " +
                    "Used during authentication to resolve the platform user from a Cognito token."
    )
    @ApiResponse(responseCode = "200", description = "Lecturer retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    AppUserResponse findLecturerByCognitoSub(@PathVariable String cognitoSub);

    // ── Student blocking (lecturer) ─────────────────────────────────────

    @PatchMapping("/students/{uuid}/block")
    @Operation(
            summary = "Block a student",
            description = "Blocks a student at the application level with a reason. " +
                    "The blocked student can still authenticate via Cognito but will be denied access to platform features. " +
                    "Used to temporarily restrict a student's access due to disciplinary or academic reasons."
    )
    @ApiResponse(responseCode = "200", description = "Student blocked successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    StudentResponse blockStudent(@PathVariable UUID uuid, @Valid @RequestBody StudentRequest.Block request);

    @PatchMapping("/students/{uuid}/unblock")
    @Operation(
            summary = "Unblock a student",
            description = "Removes the application-level block on a student, restoring their access to platform features. " +
                    "Used to reinstate a student after the reason for blocking has been resolved."
    )
    @ApiResponse(responseCode = "200", description = "Student unblocked successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    StudentResponse unblockStudent(@PathVariable UUID uuid);
}

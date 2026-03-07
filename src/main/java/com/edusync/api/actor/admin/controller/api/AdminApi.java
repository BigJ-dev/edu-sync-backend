package com.edusync.api.actor.admin.controller.api;

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

@Tag(name = "Admins", description = "Endpoints for managing platform administrators. Admins oversee the EduSync platform, manage lecturers and students, and configure system settings.")
@RequestMapping("/admins")
public interface AdminApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register a new admin",
            description = "Creates a new administrator account linked to an existing AWS Cognito identity. " +
                    "Used during onboarding after the admin has been registered in Cognito."
    )
    @ApiResponse(responseCode = "201", description = "Admin created successfully")
    @ApiResponse(responseCode = "409", description = "Email or Cognito identity already exists")
    AppUserResponse createAdmin(@Valid @RequestBody AppUserRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all admins",
            description = "Returns a paginated list of admins. Supports filtering by active status " +
                    "and searching by first name, last name, or email."
    )
    @ApiResponse(responseCode = "200", description = "Admins retrieved successfully")
    Page<AppUserResponse> findAllAdmins(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String search,
            Pageable pageable);

    @GetMapping("/{uuid}")
    @Operation(
            summary = "Get admin by UUID",
            description = "Retrieves a single admin's details by their unique identifier. " +
                    "Used to view an admin's profile information."
    )
    @ApiResponse(responseCode = "200", description = "Admin retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Admin not found")
    AppUserResponse findAdminByUuid(@PathVariable UUID uuid);

    @PutMapping("/{uuid}")
    @Operation(
            summary = "Update admin details",
            description = "Updates an admin's profile information such as name and email. " +
                    "Used when an admin's personal details need to be corrected or changed."
    )
    @ApiResponse(responseCode = "200", description = "Admin updated successfully")
    @ApiResponse(responseCode = "404", description = "Admin not found")
    AppUserResponse updateAdmin(@PathVariable UUID uuid, @Valid @RequestBody AppUserRequest.Update request);

    @GetMapping("/by-cognito-sub/{cognitoSub}")
    @Operation(
            summary = "Lookup admin by Cognito subject",
            description = "Finds an admin by their AWS Cognito subject identifier. " +
                    "Used during authentication to resolve the platform user from a Cognito token."
    )
    @ApiResponse(responseCode = "200", description = "Admin retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Admin not found")
    AppUserResponse findAdminByCognitoSub(@PathVariable String cognitoSub);

    // ── Lecturer blocking (admin only) ──────────────────────────────────

    @PatchMapping("/lecturers/{uuid}/block")
    @Operation(
            summary = "Block a lecturer",
            description = "Blocks a lecturer at the application level with a reason. Only an admin can perform this action. " +
                    "The blocked lecturer can still authenticate via Cognito but will be denied access to platform features. " +
                    "Used to temporarily restrict a lecturer's access without removing their account."
    )
    @ApiResponse(responseCode = "200", description = "Lecturer blocked successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    AppUserResponse blockLecturer(@PathVariable UUID uuid, @Valid @RequestBody AppUserRequest.Block request);

    @PatchMapping("/lecturers/{uuid}/unblock")
    @Operation(
            summary = "Unblock a lecturer",
            description = "Removes the application-level block on a lecturer, restoring their access to platform features. " +
                    "Only an admin can perform this action. " +
                    "Used to reinstate a lecturer after the reason for blocking has been resolved."
    )
    @ApiResponse(responseCode = "200", description = "Lecturer unblocked successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    AppUserResponse unblockLecturer(@PathVariable UUID uuid);

    // ── Student blocking (admin only) ───────────────────────────────────

    @PatchMapping("/students/{uuid}/block")
    @Operation(
            summary = "Block a student",
            description = "Blocks a student at the application level with a reason. " +
                    "The blocked student can still authenticate via Cognito but will be denied access to platform features. " +
                    "Used to temporarily restrict a student's access due to disciplinary or administrative reasons."
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

package com.edusync.api.admission.controller.api;

import com.edusync.api.admission.dto.ApplicationRequest;
import com.edusync.api.admission.dto.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Admissions", description = "Endpoints for managing student applications and admissions. " +
        "Students submit applications with personal information and documents. Administrators review and approve or reject applications.")
@RequestMapping("/admissions")
public interface AdmissionApi {

    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Submit a student application",
            description = "Submits a new student application with personal information, course choices (up to 3 in order of preference), " +
                    "and supporting documents. The application is created in PENDING status and awaits admin review."
    )
    @ApiResponse(responseCode = "201", description = "Application submitted successfully")
    @ApiResponse(responseCode = "409", description = "Email or ID number already in use")
    ApplicationResponse submitApplication(@Valid @RequestBody ApplicationRequest.Submit request);

    @GetMapping
    @Operation(
            summary = "List all applications",
            description = "Returns all student applications. Supports filtering by status and searching by name or email."
    )
    @ApiResponse(responseCode = "200", description = "Applications retrieved successfully")
    List<ApplicationResponse> findAllApplications(ApplicationRequest.Filter filter);

    @GetMapping("/{applicationUuid}")
    @Operation(
            summary = "Get application by UUID",
            description = "Retrieves a single student application by its unique identifier, including all uploaded documents."
    )
    @ApiResponse(responseCode = "200", description = "Application retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Application not found")
    ApplicationResponse findApplicationByUuid(@PathVariable UUID applicationUuid);

    @PatchMapping("/{applicationUuid}/review")
    @Operation(
            summary = "Review an application",
            description = "Approves or rejects a pending application. " +
                    "On approval, a student record is created with an auto-generated student number. " +
                    "On rejection, a reason must be provided."
    )
    @ApiResponse(responseCode = "200", description = "Application reviewed successfully")
    @ApiResponse(responseCode = "400", description = "Application already reviewed or invalid decision")
    @ApiResponse(responseCode = "404", description = "Application not found")
    ApplicationResponse reviewApplication(
            @PathVariable UUID applicationUuid,
            @Valid @RequestBody ApplicationRequest.Review request);
}

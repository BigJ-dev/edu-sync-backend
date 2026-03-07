package com.edusync.api.actor.student.controller.api;

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

@Tag(name = "Students", description = "Endpoints for managing students. Students are typically created through the admissions process " +
        "(see Admissions). Once approved, a student record is generated automatically. " +
        "These endpoints manage existing student profiles, enrollments, and academic progress.")
@RequestMapping("/students")
public interface StudentApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Register a new student manually",
            description = "Creates a new student account directly, bypassing the admissions process. " +
                    "Used for manual onboarding or linking an existing AWS Cognito identity. " +
                    "For the standard flow, use the Admissions endpoints to submit and approve an application."
    )
    @ApiResponse(responseCode = "201", description = "Student created successfully")
    @ApiResponse(responseCode = "409", description = "Email, Cognito identity, or student number already exists")
    StudentResponse createStudent(@Valid @RequestBody StudentRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all students",
            description = "Returns a paginated list of students. Supports filtering by active status " +
                    "and searching by first name, last name, email, or student number."
    )
    @ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    Page<StudentResponse> findAllStudents(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String search,
            Pageable pageable);

    @GetMapping("/{uuid}")
    @Operation(
            summary = "Get student by UUID",
            description = "Retrieves a single student's details by their unique identifier. " +
                    "Used to view a student's profile information and academic details."
    )
    @ApiResponse(responseCode = "200", description = "Student retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    StudentResponse findStudentByUuid(@PathVariable UUID uuid);

    @PutMapping("/{uuid}")
    @Operation(
            summary = "Update student details",
            description = "Updates a student's profile information such as name, email, phone, and student number. " +
                    "Used when a student's personal or academic details need to be corrected or changed."
    )
    @ApiResponse(responseCode = "200", description = "Student updated successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    StudentResponse updateStudent(@PathVariable UUID uuid, @Valid @RequestBody StudentRequest.Update request);

    @GetMapping("/by-cognito-sub/{cognitoSub}")
    @Operation(
            summary = "Lookup student by Cognito subject",
            description = "Finds a student by their AWS Cognito subject identifier. " +
                    "Used during authentication to resolve the platform user from a Cognito token."
    )
    @ApiResponse(responseCode = "200", description = "Student retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    StudentResponse findStudentByCognitoSub(@PathVariable String cognitoSub);

    @PatchMapping("/{uuid}/profile-image")
    @Operation(
            summary = "Update student profile image",
            description = "Updates the S3 key reference for a student's profile image. " +
                    "Called after the image has been uploaded to S3, to link it to the student's profile."
    )
    @ApiResponse(responseCode = "200", description = "Profile image updated successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    StudentResponse updateStudentProfileImage(@PathVariable UUID uuid, @Valid @RequestBody StudentRequest.ProfileImage request);
}

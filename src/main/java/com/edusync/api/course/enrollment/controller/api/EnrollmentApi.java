package com.edusync.api.course.enrollment.controller.api;

import com.edusync.api.course.enrollment.dto.EnrollmentRequest;
import com.edusync.api.course.enrollment.dto.EnrollmentResponse;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Enrollments", description = "Endpoints for managing course enrollments. Enrollments track the relationship between students and courses, including enrollment status, withdrawal, and course-level blocking.")
@RequestMapping("/courses/{courseUuid}/enrollments")
public interface EnrollmentApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Enroll a student in a course",
            description = "Creates a new enrollment linking a student to a course. " +
                    "Used when a student is registered for a course by an admin or lecturer."
    )
    @ApiResponse(responseCode = "201", description = "Student enrolled successfully")
    @ApiResponse(responseCode = "404", description = "Course or student not found")
    @ApiResponse(responseCode = "409", description = "Student is already enrolled in this course")
    EnrollmentResponse enroll(@PathVariable UUID courseUuid, @Valid @RequestBody EnrollmentRequest.Enroll request);

    @GetMapping
    @Operation(
            summary = "List all enrollments for a course",
            description = "Returns a paginated list of enrollments for a course. " +
                    "Supports filtering by enrollment status and blocked status."
    )
    @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    Page<EnrollmentResponse> findAllByCourse(
            @PathVariable UUID courseUuid,
            @RequestParam(required = false) EnrollmentStatus status,
            @RequestParam(required = false) Boolean blocked,
            Pageable pageable);

    @PatchMapping("/students/{studentUuid}/withdraw")
    @Operation(
            summary = "Withdraw a student from a course",
            description = "Marks a student's enrollment as withdrawn with a reason. " +
                    "Used when a student needs to leave a course before completion."
    )
    @ApiResponse(responseCode = "200", description = "Student withdrawn successfully")
    @ApiResponse(responseCode = "404", description = "Enrollment not found")
    EnrollmentResponse withdraw(
            @PathVariable UUID courseUuid,
            @PathVariable UUID studentUuid,
            @Valid @RequestBody EnrollmentRequest.Withdraw request);

    @PatchMapping("/students/{studentUuid}/block")
    @Operation(
            summary = "Block a student from a course",
            description = "Blocks a student's access to a specific course with a reason. " +
                    "The student remains enrolled but cannot access course content. " +
                    "Used for disciplinary or administrative restrictions at the course level."
    )
    @ApiResponse(responseCode = "200", description = "Student blocked from course successfully")
    @ApiResponse(responseCode = "404", description = "Enrollment not found")
    EnrollmentResponse block(
            @PathVariable UUID courseUuid,
            @PathVariable UUID studentUuid,
            @Valid @RequestBody EnrollmentRequest.Block request);

    @PatchMapping("/students/{studentUuid}/unblock")
    @Operation(
            summary = "Unblock a student from a course",
            description = "Removes the course-level block on a student, restoring their access to course content. " +
                    "Used to reinstate access after the reason for blocking has been resolved."
    )
    @ApiResponse(responseCode = "200", description = "Student unblocked from course successfully")
    @ApiResponse(responseCode = "404", description = "Enrollment not found")
    EnrollmentResponse unblock(@PathVariable UUID courseUuid, @PathVariable UUID studentUuid);
}

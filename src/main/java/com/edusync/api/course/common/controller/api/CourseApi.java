package com.edusync.api.course.common.controller.api;

import com.edusync.api.course.common.dto.CourseRequest;
import com.edusync.api.course.common.dto.CourseResponse;
import com.edusync.api.course.common.enums.CourseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Courses", description = "Endpoints for managing courses. Courses are the primary learning containers on the EduSync platform, owned by a lecturer and containing modules, sessions, and materials.")
@RequestMapping("/courses")
public interface CourseApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new course",
            description = "Creates a new course in DRAFT status, assigned to a specific lecturer. " +
                    "Used when a lecturer or admin sets up a new learning programme."
    )
    @ApiResponse(responseCode = "201", description = "Course created successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    @ApiResponse(responseCode = "409", description = "Course code already exists")
    CourseResponse create(@Valid @RequestBody CourseRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all courses",
            description = "Returns a paginated list of courses. Supports filtering by status " +
                    "and searching by course code or title."
    )
    @ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    Page<CourseResponse> findAll(
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(required = false) String search,
            Pageable pageable);

    @GetMapping("/{uuid}")
    @Operation(
            summary = "Get course by UUID",
            description = "Retrieves a single course's details by its unique identifier. " +
                    "Used to view course information, configuration, and current status."
    )
    @ApiResponse(responseCode = "200", description = "Course retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    CourseResponse findByUuid(@PathVariable UUID uuid);

    @PutMapping("/{uuid}")
    @Operation(
            summary = "Update course details",
            description = "Updates a course's information such as title, description, dates, and capacity. " +
                    "Used when course details need to be corrected or adjusted before or during delivery."
    )
    @ApiResponse(responseCode = "200", description = "Course updated successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    CourseResponse update(@PathVariable UUID uuid, @Valid @RequestBody CourseRequest.Update request);

    @PatchMapping("/{uuid}/status")
    @Operation(
            summary = "Update course status",
            description = "Changes the lifecycle status of a course (e.g. DRAFT to PUBLISHED, or IN_PROGRESS to COMPLETED). " +
                    "Used to manage the course lifecycle as it progresses through delivery stages."
    )
    @ApiResponse(responseCode = "200", description = "Course status updated successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    CourseResponse updateStatus(@PathVariable UUID uuid, @Valid @RequestBody CourseRequest.UpdateStatus request);
}

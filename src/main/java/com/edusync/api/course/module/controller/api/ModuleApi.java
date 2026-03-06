package com.edusync.api.course.module.controller.api;

import com.edusync.api.course.module.dto.ModuleRequest;
import com.edusync.api.course.module.dto.ModuleResponse;
import com.edusync.api.course.module.enums.ModuleStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Modules", description = "Endpoints for managing course modules. Modules are the organizing layer within a course, containing sessions, materials, assessments, and quizzes.")
@RequestMapping("/courses/{courseUuid}/modules")
public interface ModuleApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new module",
            description = "Creates a new module within a course in DRAFT status. " +
                    "Modules are ordered by sort order and represent logical sections of a course."
    )
    @ApiResponse(responseCode = "201", description = "Module created successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    @ApiResponse(responseCode = "409", description = "Sort order already taken in this course")
    ModuleResponse create(@PathVariable UUID courseUuid, @Valid @RequestBody ModuleRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all modules for a course",
            description = "Returns all modules belonging to a course. Supports filtering by status " +
                    "and searching by module title. Results are ordered by sort order."
    )
    @ApiResponse(responseCode = "200", description = "Modules retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    List<ModuleResponse> findAllByCourse(
            @PathVariable UUID courseUuid,
            @RequestParam(required = false) ModuleStatus status,
            @RequestParam(required = false) String search);

    @GetMapping("/{moduleUuid}")
    @Operation(
            summary = "Get module by UUID",
            description = "Retrieves a single module's details by its unique identifier. " +
                    "Used to view module information and current status."
    )
    @ApiResponse(responseCode = "200", description = "Module retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Module not found")
    ModuleResponse findByUuid(@PathVariable UUID courseUuid, @PathVariable UUID moduleUuid);

    @PutMapping("/{moduleUuid}")
    @Operation(
            summary = "Update module details",
            description = "Updates a module's information such as title, description, sort order, and dates. " +
                    "Used when module details need to be corrected or restructured."
    )
    @ApiResponse(responseCode = "200", description = "Module updated successfully")
    @ApiResponse(responseCode = "404", description = "Module not found")
    @ApiResponse(responseCode = "409", description = "Sort order already taken in this course")
    ModuleResponse update(@PathVariable UUID courseUuid, @PathVariable UUID moduleUuid, @Valid @RequestBody ModuleRequest.Update request);

    @PatchMapping("/{moduleUuid}/status")
    @Operation(
            summary = "Update module status",
            description = "Changes the lifecycle status of a module (e.g. DRAFT to PUBLISHED, or PUBLISHED to LOCKED). " +
                    "Used to control student access to module content as the course progresses."
    )
    @ApiResponse(responseCode = "200", description = "Module status updated successfully")
    @ApiResponse(responseCode = "404", description = "Module not found")
    ModuleResponse updateStatus(@PathVariable UUID courseUuid, @PathVariable UUID moduleUuid, @Valid @RequestBody ModuleRequest.UpdateStatus request);
}

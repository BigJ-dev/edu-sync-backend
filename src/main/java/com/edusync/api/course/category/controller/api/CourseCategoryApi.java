package com.edusync.api.course.category.controller.api;

import com.edusync.api.course.category.dto.CategoryRequest;
import com.edusync.api.course.category.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Course Categories", description = "Endpoints for managing course categories and category-course mappings.")
@RequestMapping("/categories")
public interface CourseCategoryApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new course category", description = "Creates a new course category. Optionally nested under a parent category.")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @ApiResponse(responseCode = "404", description = "Parent category not found")
    @ApiResponse(responseCode = "409", description = "Category name already exists under this parent")
    CategoryResponse create(@Valid @RequestBody CategoryRequest.Create request);

    @GetMapping
    @Operation(summary = "List all categories", description = "Returns all categories. Supports filtering by active status and searching by name.")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    List<CategoryResponse> findAll(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String search);

    @GetMapping("/roots")
    @Operation(summary = "List root categories", description = "Returns all top-level categories that have no parent, ordered by sort order.")
    @ApiResponse(responseCode = "200", description = "Root categories retrieved successfully")
    List<CategoryResponse> findRoots();

    @GetMapping("/{categoryUuid}")
    @Operation(summary = "Get category by UUID", description = "Retrieves a single category by its unique identifier.")
    @ApiResponse(responseCode = "200", description = "Category retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    CategoryResponse findByUuid(@PathVariable UUID categoryUuid);

    @GetMapping("/{categoryUuid}/children")
    @Operation(summary = "List child categories", description = "Returns all direct child categories of a given parent category, ordered by sort order.")
    @ApiResponse(responseCode = "200", description = "Child categories retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Parent category not found")
    List<CategoryResponse> findChildren(@PathVariable UUID categoryUuid);

    @PutMapping("/{categoryUuid}")
    @Operation(summary = "Update a category", description = "Updates an existing category's details.")
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "409", description = "Category name already exists under this parent")
    CategoryResponse update(@PathVariable UUID categoryUuid, @Valid @RequestBody CategoryRequest.Update request);

    @DeleteMapping("/{categoryUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category", description = "Deletes a category by its UUID.")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    void delete(@PathVariable UUID categoryUuid);

    @PostMapping("/{categoryUuid}/courses")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Assign a course to a category", description = "Creates a mapping between a course and a category.")
    @ApiResponse(responseCode = "201", description = "Course assigned to category successfully")
    @ApiResponse(responseCode = "404", description = "Category or course not found")
    @ApiResponse(responseCode = "409", description = "Course is already assigned to this category")
    CategoryResponse assignCourse(@PathVariable UUID categoryUuid, @Valid @RequestBody CategoryRequest.AssignCourse request);

    @DeleteMapping("/{categoryUuid}/courses/{courseUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Unassign a course from a category", description = "Removes the mapping between a course and a category.")
    @ApiResponse(responseCode = "204", description = "Course unassigned from category successfully")
    @ApiResponse(responseCode = "404", description = "Category, course, or mapping not found")
    void unassignCourse(@PathVariable UUID categoryUuid, @PathVariable UUID courseUuid);

    @GetMapping("/by-course/{courseUuid}")
    @Operation(summary = "List categories for a course", description = "Returns all categories assigned to a specific course.")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    List<CategoryResponse> findCourseCategories(@PathVariable UUID courseUuid);
}

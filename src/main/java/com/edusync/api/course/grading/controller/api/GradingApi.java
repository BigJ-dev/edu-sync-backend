package com.edusync.api.course.grading.controller.api;

import com.edusync.api.course.grading.dto.FinalGradeResponse;
import com.edusync.api.course.grading.dto.GradeWeightRequest;
import com.edusync.api.course.grading.dto.GradeWeightResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Grading", description = "Grade weight categories and weighted final grade calculation. " +
        "Define how assessments and quizzes contribute to the final course grade (e.g., Assignments 40%, Quizzes 20%, Exam 40%).")
@RequestMapping("/courses/{courseUuid}/grading")
public interface GradingApi {

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a grade weight category",
            description = "Adds a new grade weight category to a course (e.g., 'Assignments' at 40%). " +
                    "Each category type can only appear once per course."
    )
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    GradeWeightResponse createCategory(@PathVariable UUID courseUuid, @Valid @RequestBody GradeWeightRequest.Create request);

    @PutMapping("/categories/{categoryUuid}")
    @Operation(
            summary = "Update a grade weight category",
            description = "Updates the name, weight percentage, or sort order of a grade weight category."
    )
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    GradeWeightResponse updateCategory(@PathVariable UUID courseUuid, @PathVariable UUID categoryUuid, @Valid @RequestBody GradeWeightRequest.Update request);

    @DeleteMapping("/categories/{categoryUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a grade weight category",
            description = "Removes a grade weight category from the course."
    )
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "404", description = "Category not found")
    void deleteCategory(@PathVariable UUID courseUuid, @PathVariable UUID categoryUuid);

    @GetMapping("/categories")
    @Operation(
            summary = "List grade weight categories",
            description = "Returns all grade weight categories for a course, ordered by sort order. " +
                    "The weights should sum to 100% for a complete grading scheme."
    )
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    List<GradeWeightResponse> findCategories(@PathVariable UUID courseUuid);

    @GetMapping("/students/{studentUuid}/final-grade")
    @Operation(
            summary = "Calculate weighted final grade",
            description = "Calculates a student's weighted final grade for a course. " +
                    "Aggregates assessment submissions and quiz attempts by category, " +
                    "computes the average percentage per category, and applies the configured weights. " +
                    "Returns the breakdown per category and the overall final grade percentage."
    )
    @ApiResponse(responseCode = "200", description = "Final grade calculated successfully")
    @ApiResponse(responseCode = "404", description = "Course or student not found")
    @ApiResponse(responseCode = "422", description = "No grade weight categories configured")
    FinalGradeResponse calculateFinalGrade(@PathVariable UUID courseUuid, @PathVariable UUID studentUuid);
}

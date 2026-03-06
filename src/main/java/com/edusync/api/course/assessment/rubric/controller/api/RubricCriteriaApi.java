package com.edusync.api.course.assessment.rubric.controller.api;

import com.edusync.api.course.assessment.rubric.dto.RubricCriteriaRequest;
import com.edusync.api.course.assessment.rubric.dto.RubricCriteriaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Rubric Criteria", description = "Endpoints for managing assessment rubric criteria. Used by the assessment builder UI to define grading criteria. Each criterion has a title, description, max marks, and sort order. The sum of all criteria max_marks is validated against assessment total_marks when publishing.")
@RequestMapping("/assessments/{assessmentUuid}/rubric")
public interface RubricCriteriaApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add a rubric criterion",
            description = "Adds a new grading criterion to an assessment's rubric. " +
                    "Used by the assessment builder UI to define how the assessment will be graded."
    )
    @ApiResponse(responseCode = "201", description = "Rubric criterion created successfully")
    @ApiResponse(responseCode = "404", description = "Assessment not found")
    @ApiResponse(responseCode = "409", description = "Sort order already taken")
    RubricCriteriaResponse create(@PathVariable UUID assessmentUuid, @Valid @RequestBody RubricCriteriaRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all rubric criteria for an assessment",
            description = "Returns all rubric criteria for an assessment, ordered by sort order. " +
                    "Used by the builder UI to display the current rubric configuration."
    )
    @ApiResponse(responseCode = "200", description = "Rubric criteria retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Assessment not found")
    List<RubricCriteriaResponse> findAllByAssessment(@PathVariable UUID assessmentUuid);

    @PutMapping("/{criteriaUuid}")
    @Operation(
            summary = "Update a rubric criterion",
            description = "Updates a rubric criterion's title, description, max marks, or sort order. " +
                    "Used by the builder UI when editing criteria."
    )
    @ApiResponse(responseCode = "200", description = "Rubric criterion updated successfully")
    @ApiResponse(responseCode = "404", description = "Rubric criterion not found")
    @ApiResponse(responseCode = "409", description = "Sort order already taken")
    RubricCriteriaResponse update(@PathVariable UUID assessmentUuid, @PathVariable UUID criteriaUuid, @Valid @RequestBody RubricCriteriaRequest.Update request);

    @DeleteMapping("/{criteriaUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a rubric criterion",
            description = "Removes a rubric criterion from the assessment. " +
                    "Used by the builder UI when removing criteria."
    )
    @ApiResponse(responseCode = "204", description = "Rubric criterion deleted successfully")
    @ApiResponse(responseCode = "404", description = "Rubric criterion not found")
    void delete(@PathVariable UUID assessmentUuid, @PathVariable UUID criteriaUuid);
}

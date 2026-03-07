package com.edusync.api.course.assessment.common.controller.api;

import com.edusync.api.course.assessment.common.dto.AssessmentRequest;
import com.edusync.api.course.assessment.common.dto.AssessmentResponse;
import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.common.enums.DeliveryMode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Assessments", description = "Endpoints for managing assessments. Assessments are graded work items (assignments, projects, practicals, presentations) within a module. Supports both ONLINE (submit via editor/upload) and PHYSICAL (submit document back) delivery modes. Use the rubric criteria endpoints to build the assessment rubric via the builder UI.")
@RequestMapping("/modules/{moduleUuid}/assessments")
public interface AssessmentApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new assessment",
            description = "Creates a new assessment within a module in DRAFT status. " +
                    "The assessment can be ONLINE (students submit via editor or file upload) " +
                    "or PHYSICAL (students submit a physical document). " +
                    "After creating, use the rubric criteria endpoints to build the grading rubric."
    )
    @ApiResponse(responseCode = "201", description = "Assessment created successfully")
    @ApiResponse(responseCode = "404", description = "Module or creator not found")
    AssessmentResponse create(@PathVariable UUID moduleUuid, @Valid @RequestBody AssessmentRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all assessments for a module",
            description = "Returns all assessments belonging to a module. Supports filtering by status, " +
                    "assessment type, delivery mode, and searching by title."
    )
    @ApiResponse(responseCode = "200", description = "Assessments retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Module not found")
    List<AssessmentResponse> findAllByModule(
            @PathVariable UUID moduleUuid,
            @RequestParam(required = false) AssessmentStatus status,
            @RequestParam(required = false) AssessmentType type,
            @RequestParam(required = false) DeliveryMode mode,
            @RequestParam(required = false) String search);

    @GetMapping("/{assessmentUuid}")
    @Operation(
            summary = "Get assessment by UUID",
            description = "Retrieves a single assessment's details including configuration, " +
                    "delivery mode, marks, and deadline information."
    )
    @ApiResponse(responseCode = "200", description = "Assessment retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Assessment not found")
    AssessmentResponse findByUuid(@PathVariable UUID moduleUuid, @PathVariable UUID assessmentUuid);

    @PutMapping("/{assessmentUuid}")
    @Operation(
            summary = "Update assessment details",
            description = "Updates an assessment's configuration including title, marks, deadline, delivery mode, " +
                    "and late submission settings. Used by the assessment builder UI to save changes."
    )
    @ApiResponse(responseCode = "200", description = "Assessment updated successfully")
    @ApiResponse(responseCode = "404", description = "Assessment not found")
    AssessmentResponse update(@PathVariable UUID moduleUuid, @PathVariable UUID assessmentUuid, @Valid @RequestBody AssessmentRequest.Update request);

    @PatchMapping("/{assessmentUuid}/status")
    @Operation(
            summary = "Update assessment status",
            description = "Changes the lifecycle status of an assessment (e.g. DRAFT to PUBLISHED, or CLOSED to GRADED). " +
                    "Publishing validates that rubric criteria total matches assessment total marks (if rubric exists). " +
                    "PUBLISHED makes the assessment visible to students."
    )
    @ApiResponse(responseCode = "200", description = "Assessment status updated successfully")
    @ApiResponse(responseCode = "404", description = "Assessment not found")
    AssessmentResponse updateStatus(@PathVariable UUID moduleUuid, @PathVariable UUID assessmentUuid, @Valid @RequestBody AssessmentRequest.UpdateStatus request);

    @DeleteMapping("/{assessmentUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an assessment", description = "Permanently deletes an assessment and its submissions.")
    @ApiResponse(responseCode = "204", description = "Assessment deleted successfully")
    @ApiResponse(responseCode = "404", description = "Assessment not found")
    void delete(@PathVariable UUID moduleUuid, @PathVariable UUID assessmentUuid);

    @PostMapping("/{assessmentUuid}/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Duplicate an assessment",
            description = "Creates a copy of an assessment in DRAFT status. Optionally specify a target module UUID " +
                    "to duplicate into a different module/course. Copies all settings but not submissions or rubric grades."
    )
    @ApiResponse(responseCode = "201", description = "Assessment duplicated successfully")
    @ApiResponse(responseCode = "404", description = "Assessment or target module not found")
    AssessmentResponse duplicate(@PathVariable UUID moduleUuid, @PathVariable UUID assessmentUuid,
                                  @RequestParam(required = false) UUID targetModuleUuid);

    @PostMapping("/{assessmentUuid}/reopen")
    @Operation(
            summary = "Reopen an assessment",
            description = "Reopens a closed or graded assessment with a new due date, making it available " +
                    "for students to submit again. Useful for giving students another chance."
    )
    @ApiResponse(responseCode = "200", description = "Assessment reopened successfully")
    @ApiResponse(responseCode = "404", description = "Assessment not found")
    AssessmentResponse reopen(@PathVariable UUID moduleUuid, @PathVariable UUID assessmentUuid,
                               @Valid @RequestBody AssessmentRequest.Reopen request);
}

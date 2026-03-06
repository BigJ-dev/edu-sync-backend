package com.edusync.api.course.assessment.grade.controller.api;

import com.edusync.api.course.assessment.grade.dto.RubricGradeRequest;
import com.edusync.api.course.assessment.grade.dto.RubricGradeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Rubric Grades", description = "Endpoints for grading submissions per rubric criterion. Each criterion can be graded individually. The DB trigger auto-computes the total marks on submission and auto-sets status to GRADED when all criteria are graded.")
@RequestMapping("/submissions/{submissionUuid}/grades")
public interface RubricGradeApi {

    @PutMapping("/criteria/{criteriaUuid}")
    @Operation(
            summary = "Grade a rubric criterion",
            description = "Awards marks for a specific rubric criterion on a submission. " +
                    "If already graded, updates the existing grade. The DB trigger automatically " +
                    "recomputes submission.marks_obtained and sets status to GRADED when all criteria are done."
    )
    @ApiResponse(responseCode = "200", description = "Criterion graded successfully")
    @ApiResponse(responseCode = "404", description = "Submission, criterion, or grader not found")
    RubricGradeResponse award(
            @PathVariable UUID submissionUuid,
            @PathVariable UUID criteriaUuid,
            @Valid @RequestBody RubricGradeRequest.Award request);

    @GetMapping
    @Operation(
            summary = "List all rubric grades for a submission",
            description = "Returns all per-criterion grades for a submission. " +
                    "Used to display the grading breakdown in the lecturer's grading view."
    )
    @ApiResponse(responseCode = "200", description = "Rubric grades retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Submission not found")
    List<RubricGradeResponse> findAllBySubmission(@PathVariable UUID submissionUuid);
}

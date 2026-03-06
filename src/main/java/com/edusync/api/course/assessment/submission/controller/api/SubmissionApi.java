package com.edusync.api.course.assessment.submission.controller.api;

import com.edusync.api.course.assessment.submission.dto.SubmissionRequest;
import com.edusync.api.course.assessment.submission.dto.SubmissionResponse;
import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Submissions", description = "Endpoints for managing assessment submissions. Students submit work either via online editor (submissionText) for ONLINE assessments, or upload a document (s3Key/fileName) for PHYSICAL assessments. Lecturers can then grade directly or use the rubric grading system.")
@RequestMapping("/assessments/{assessmentUuid}/submissions")
public interface SubmissionApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Submit an assessment",
            description = "Submits work for an assessment. For ONLINE delivery, provide submissionText " +
                    "(content from the online editor). For PHYSICAL delivery, provide s3Key and fileName " +
                    "(uploaded document). Automatically detects late submissions and enforces late submission policy."
    )
    @ApiResponse(responseCode = "201", description = "Submission created successfully")
    @ApiResponse(responseCode = "400", description = "Late submission not allowed")
    @ApiResponse(responseCode = "404", description = "Assessment or student not found")
    @ApiResponse(responseCode = "409", description = "Student has already submitted")
    SubmissionResponse submit(@PathVariable UUID assessmentUuid, @Valid @RequestBody SubmissionRequest.Submit request);

    @GetMapping
    @Operation(
            summary = "List all submissions for an assessment",
            description = "Returns all submissions for an assessment. Supports filtering by status and late flag. " +
                    "Used by lecturers to view and manage student submissions."
    )
    @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Assessment not found")
    List<SubmissionResponse> findAllByAssessment(
            @PathVariable UUID assessmentUuid,
            @RequestParam(required = false) SubmissionStatus status,
            @RequestParam(required = false) Boolean late);

    @GetMapping("/{submissionUuid}")
    @Operation(
            summary = "Get submission by UUID",
            description = "Retrieves a single submission's details including content, grade, and feedback."
    )
    @ApiResponse(responseCode = "200", description = "Submission retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Submission not found")
    SubmissionResponse findByUuid(@PathVariable UUID assessmentUuid, @PathVariable UUID submissionUuid);

    @PatchMapping("/{submissionUuid}/grade")
    @Operation(
            summary = "Grade a submission",
            description = "Directly grades a submission with marks and feedback (without rubric). " +
                    "Sets the submission status to GRADED. For rubric-based grading, " +
                    "use the rubric grade endpoints instead."
    )
    @ApiResponse(responseCode = "200", description = "Submission graded successfully")
    @ApiResponse(responseCode = "404", description = "Submission not found")
    SubmissionResponse grade(@PathVariable UUID assessmentUuid, @PathVariable UUID submissionUuid, @Valid @RequestBody SubmissionRequest.Grade request);

    @PatchMapping("/{submissionUuid}/return")
    @Operation(
            summary = "Return a submission for resubmission",
            description = "Returns a submission to the student with feedback, allowing them to resubmit. " +
                    "Sets the submission status to RETURNED."
    )
    @ApiResponse(responseCode = "200", description = "Submission returned successfully")
    @ApiResponse(responseCode = "404", description = "Submission not found")
    SubmissionResponse returnForResubmission(@PathVariable UUID assessmentUuid, @PathVariable UUID submissionUuid, @Valid @RequestBody SubmissionRequest.Return request);
}

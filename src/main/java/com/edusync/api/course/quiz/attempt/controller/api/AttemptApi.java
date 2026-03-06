package com.edusync.api.course.quiz.attempt.controller.api;

import com.edusync.api.course.quiz.attempt.dto.AnswerResponse;
import com.edusync.api.course.quiz.attempt.dto.AttemptRequest;
import com.edusync.api.course.quiz.attempt.dto.AttemptResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Quiz Attempts", description = "Endpoints for managing quiz attempts. Students start attempts, submit answers, and complete attempts. Auto-grades MULTIPLE_CHOICE, TRUE_FALSE, and MULTI_SELECT questions. SHORT_ANSWER questions require manual grading.")
@RequestMapping("/quizzes/{quizUuid}/attempts")
public interface AttemptApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Start a quiz attempt",
            description = "Starts a new quiz attempt for a student. Validates that the student has not exceeded the maximum number of attempts."
    )
    @ApiResponse(responseCode = "201", description = "Attempt started successfully")
    @ApiResponse(responseCode = "400", description = "Maximum attempts reached")
    @ApiResponse(responseCode = "404", description = "Quiz or student not found")
    AttemptResponse startAttempt(@PathVariable UUID quizUuid, @Valid @RequestBody AttemptRequest.Start request);

    @GetMapping
    @Operation(
            summary = "List attempts for a quiz",
            description = "Returns all attempts for a quiz. Optionally filter by student UUID."
    )
    @ApiResponse(responseCode = "200", description = "Attempts retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Quiz not found")
    List<AttemptResponse> findAttempts(
            @PathVariable UUID quizUuid,
            @RequestParam(required = false) UUID studentUuid);

    @GetMapping("/{attemptUuid}")
    @Operation(
            summary = "Get attempt by UUID",
            description = "Retrieves a single attempt's details including score, status, and timing information."
    )
    @ApiResponse(responseCode = "200", description = "Attempt retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Attempt not found")
    AttemptResponse findByUuid(@PathVariable UUID quizUuid, @PathVariable UUID attemptUuid);

    @PostMapping("/{attemptUuid}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Submit an answer",
            description = "Submits an answer for a question in this attempt. Auto-grades MULTIPLE_CHOICE, TRUE_FALSE, " +
                    "and MULTI_SELECT questions. SHORT_ANSWER questions are marked as requiring manual grading."
    )
    @ApiResponse(responseCode = "201", description = "Answer submitted successfully")
    @ApiResponse(responseCode = "400", description = "Attempt is not in progress")
    @ApiResponse(responseCode = "404", description = "Attempt, question, or option not found")
    AnswerResponse submitAnswer(@PathVariable UUID quizUuid, @PathVariable UUID attemptUuid, @Valid @RequestBody AttemptRequest.SubmitAnswer request);

    @PostMapping("/{attemptUuid}/complete")
    @Operation(
            summary = "Complete an attempt",
            description = "Finalizes an attempt by calculating the total score, score percentage, and pass/fail status. " +
                    "Sets the attempt status to COMPLETED."
    )
    @ApiResponse(responseCode = "200", description = "Attempt completed successfully")
    @ApiResponse(responseCode = "400", description = "Attempt is not in progress")
    @ApiResponse(responseCode = "404", description = "Attempt not found")
    AttemptResponse completeAttempt(@PathVariable UUID quizUuid, @PathVariable UUID attemptUuid);

    @GetMapping("/{attemptUuid}/answers")
    @Operation(
            summary = "List answers for an attempt",
            description = "Returns all answers submitted for this attempt, including correctness and marks awarded."
    )
    @ApiResponse(responseCode = "200", description = "Answers retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Attempt not found")
    List<AnswerResponse> findAnswers(@PathVariable UUID quizUuid, @PathVariable UUID attemptUuid);
}

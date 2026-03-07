package com.edusync.api.course.quiz.common.controller.api;

import com.edusync.api.course.quiz.common.dto.QuizRequest;
import com.edusync.api.course.quiz.common.dto.QuizResponse;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Quizzes", description = "Endpoints for managing quizzes within a module. Quizzes support multiple question types (MULTIPLE_CHOICE, MULTI_SELECT, TRUE_FALSE, SHORT_ANSWER), configurable time limits, multiple attempts, and automatic grading.")
@RequestMapping("/modules/{moduleUuid}/quizzes")
public interface QuizApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new quiz",
            description = "Creates a new quiz within a module in DRAFT status. " +
                    "After creating, use the quiz questions endpoints to add questions and options."
    )
    @ApiResponse(responseCode = "201", description = "Quiz created successfully")
    @ApiResponse(responseCode = "404", description = "Module, creator, or class session not found")
    QuizResponse create(@PathVariable UUID moduleUuid, @Valid @RequestBody QuizRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all quizzes for a module",
            description = "Returns all quizzes belonging to a module. Supports filtering by status and searching by title."
    )
    @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Module not found")
    List<QuizResponse> findAllByModule(
            @PathVariable UUID moduleUuid,
            @RequestParam(required = false) QuizStatus status,
            @RequestParam(required = false) String search);

    @GetMapping("/{quizUuid}")
    @Operation(
            summary = "Get quiz by UUID",
            description = "Retrieves a single quiz's details including configuration, marks, and visibility settings."
    )
    @ApiResponse(responseCode = "200", description = "Quiz retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Quiz not found")
    QuizResponse findByUuid(@PathVariable UUID moduleUuid, @PathVariable UUID quizUuid);

    @PutMapping("/{quizUuid}")
    @Operation(
            summary = "Update quiz details",
            description = "Updates a quiz's configuration including title, marks, time limit, and visibility settings."
    )
    @ApiResponse(responseCode = "200", description = "Quiz updated successfully")
    @ApiResponse(responseCode = "404", description = "Quiz not found")
    QuizResponse update(@PathVariable UUID moduleUuid, @PathVariable UUID quizUuid, @Valid @RequestBody QuizRequest.Update request);

    @PatchMapping("/{quizUuid}/status")
    @Operation(
            summary = "Update quiz status",
            description = "Changes the lifecycle status of a quiz (e.g. DRAFT to PUBLISHED, or PUBLISHED to CLOSED). " +
                    "PUBLISHED makes the quiz visible to students."
    )
    @ApiResponse(responseCode = "200", description = "Quiz status updated successfully")
    @ApiResponse(responseCode = "404", description = "Quiz not found")
    QuizResponse updateStatus(@PathVariable UUID moduleUuid, @PathVariable UUID quizUuid, @Valid @RequestBody QuizRequest.UpdateStatus request);

    @DeleteMapping("/{quizUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a quiz", description = "Permanently deletes a quiz and its questions, options, and attempts.")
    @ApiResponse(responseCode = "204", description = "Quiz deleted successfully")
    @ApiResponse(responseCode = "404", description = "Quiz not found")
    void delete(@PathVariable UUID moduleUuid, @PathVariable UUID quizUuid);

    @PostMapping("/{quizUuid}/duplicate")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Duplicate a quiz",
            description = "Creates a copy of a quiz in DRAFT status. Optionally specify a target module UUID " +
                    "to duplicate into a different module/course. Copies all settings but not questions or attempts."
    )
    @ApiResponse(responseCode = "201", description = "Quiz duplicated successfully")
    @ApiResponse(responseCode = "404", description = "Quiz or target module not found")
    QuizResponse duplicate(@PathVariable UUID moduleUuid, @PathVariable UUID quizUuid,
                            @RequestParam(required = false) UUID targetModuleUuid);

    @PostMapping("/{quizUuid}/reopen")
    @Operation(
            summary = "Reopen a quiz",
            description = "Reopens a closed quiz with a new visibility window, making it available " +
                    "for students to attempt again. Optionally grants additional attempts."
    )
    @ApiResponse(responseCode = "200", description = "Quiz reopened successfully")
    @ApiResponse(responseCode = "404", description = "Quiz not found")
    QuizResponse reopen(@PathVariable UUID moduleUuid, @PathVariable UUID quizUuid,
                         @Valid @RequestBody QuizRequest.Reopen request);
}

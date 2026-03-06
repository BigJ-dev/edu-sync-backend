package com.edusync.api.course.quiz.question.controller.api;

import com.edusync.api.course.quiz.question.dto.OptionResponse;
import com.edusync.api.course.quiz.question.dto.QuestionRequest;
import com.edusync.api.course.quiz.question.dto.QuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Quiz Questions", description = "Endpoints for managing quiz questions and their options. Supports MULTIPLE_CHOICE, MULTI_SELECT, TRUE_FALSE, and SHORT_ANSWER question types.")
@RequestMapping("/quizzes/{quizUuid}/questions")
public interface QuestionApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add a question to a quiz",
            description = "Adds a new question with options to a quiz. The request body includes the question details and a list of options."
    )
    @ApiResponse(responseCode = "201", description = "Question created successfully")
    @ApiResponse(responseCode = "404", description = "Quiz not found")
    QuestionResponse addQuestion(@PathVariable UUID quizUuid, @Valid @RequestBody QuestionRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List all questions for a quiz",
            description = "Returns all questions for a quiz ordered by sort order, each with their options."
    )
    @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Quiz not found")
    List<QuestionResponse> findByQuiz(@PathVariable UUID quizUuid);

    @GetMapping("/{questionUuid}")
    @Operation(
            summary = "Get question by UUID",
            description = "Retrieves a single question with its options."
    )
    @ApiResponse(responseCode = "200", description = "Question retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Question not found")
    QuestionResponse findByUuid(@PathVariable UUID quizUuid, @PathVariable UUID questionUuid);

    @PutMapping("/{questionUuid}")
    @Operation(
            summary = "Update a question",
            description = "Updates a question's text, marks, sort order, and explanation. Does not change the question type."
    )
    @ApiResponse(responseCode = "200", description = "Question updated successfully")
    @ApiResponse(responseCode = "404", description = "Question not found")
    QuestionResponse updateQuestion(@PathVariable UUID quizUuid, @PathVariable UUID questionUuid, @Valid @RequestBody QuestionRequest.Update request);

    @DeleteMapping("/{questionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a question",
            description = "Deletes a question and all its options from a quiz."
    )
    @ApiResponse(responseCode = "204", description = "Question deleted successfully")
    @ApiResponse(responseCode = "404", description = "Question not found")
    void deleteQuestion(@PathVariable UUID quizUuid, @PathVariable UUID questionUuid);

    @PutMapping("/{questionUuid}/options")
    @Operation(
            summary = "Replace all options for a question",
            description = "Deletes all existing options and creates new ones. Used to bulk-update the options for a question."
    )
    @ApiResponse(responseCode = "200", description = "Options replaced successfully")
    @ApiResponse(responseCode = "404", description = "Question not found")
    List<OptionResponse> replaceOptions(@PathVariable UUID quizUuid, @PathVariable UUID questionUuid, @Valid @RequestBody List<QuestionRequest.OptionCreate> options);
}

package com.edusync.api.course.quiz.attempt.controller;

import com.edusync.api.course.quiz.attempt.controller.api.AttemptApi;
import com.edusync.api.course.quiz.attempt.dto.AnswerResponse;
import com.edusync.api.course.quiz.attempt.dto.AttemptRequest;
import com.edusync.api.course.quiz.attempt.dto.AttemptResponse;
import com.edusync.api.course.quiz.attempt.service.AttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class AttemptController implements AttemptApi {

    private final AttemptService service;

    @Override
    public AttemptResponse startAttempt(UUID quizUuid, AttemptRequest.Start request) {
        return service.startAttempt(quizUuid, request);
    }

    @Override
    public List<AttemptResponse> findAttempts(UUID quizUuid, UUID studentUuid) {
        return service.findAttemptsByQuizAndStudent(quizUuid, studentUuid);
    }

    @Override
    public AttemptResponse findByUuid(UUID quizUuid, UUID attemptUuid) {
        return service.findByUuid(attemptUuid);
    }

    @Override
    public AnswerResponse submitAnswer(UUID quizUuid, UUID attemptUuid, AttemptRequest.SubmitAnswer request) {
        return service.submitAnswer(attemptUuid, request);
    }

    @Override
    public AttemptResponse completeAttempt(UUID quizUuid, UUID attemptUuid) {
        return service.completeAttempt(attemptUuid);
    }

    @Override
    public List<AnswerResponse> findAnswers(UUID quizUuid, UUID attemptUuid) {
        return service.findAnswers(attemptUuid);
    }
}

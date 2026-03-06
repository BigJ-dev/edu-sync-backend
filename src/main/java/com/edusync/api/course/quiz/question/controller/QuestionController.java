package com.edusync.api.course.quiz.question.controller;

import com.edusync.api.course.quiz.question.controller.api.QuestionApi;
import com.edusync.api.course.quiz.question.dto.OptionResponse;
import com.edusync.api.course.quiz.question.dto.QuestionRequest;
import com.edusync.api.course.quiz.question.dto.QuestionResponse;
import com.edusync.api.course.quiz.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class QuestionController implements QuestionApi {

    private final QuestionService service;

    @Override
    public QuestionResponse addQuestion(UUID quizUuid, QuestionRequest.Create request) {
        return service.addQuestion(quizUuid, request);
    }

    @Override
    public List<QuestionResponse> findByQuiz(UUID quizUuid) {
        return service.findByQuiz(quizUuid);
    }

    @Override
    public QuestionResponse findByUuid(UUID quizUuid, UUID questionUuid) {
        return service.findByUuid(questionUuid);
    }

    @Override
    public QuestionResponse updateQuestion(UUID quizUuid, UUID questionUuid, QuestionRequest.Update request) {
        return service.updateQuestion(questionUuid, request);
    }

    @Override
    public void deleteQuestion(UUID quizUuid, UUID questionUuid) {
        service.deleteQuestion(questionUuid);
    }

    @Override
    public List<OptionResponse> replaceOptions(UUID quizUuid, UUID questionUuid, List<QuestionRequest.OptionCreate> options) {
        return service.replaceOptions(questionUuid, options);
    }
}

package com.edusync.api.course.quiz.common.controller;

import com.edusync.api.course.quiz.common.controller.api.QuizApi;
import com.edusync.api.course.quiz.common.dto.QuizRequest;
import com.edusync.api.course.quiz.common.dto.QuizResponse;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.course.quiz.common.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class QuizController implements QuizApi {

    private final QuizService service;

    @Override
    public QuizResponse create(UUID moduleUuid, QuizRequest.Create request) {
        return service.createQuiz(moduleUuid, request);
    }

    @Override
    public List<QuizResponse> findAllByModule(UUID moduleUuid, QuizStatus status, String search) {
        return service.findAllQuizzesByModule(moduleUuid, status, search);
    }

    @Override
    public QuizResponse findByUuid(UUID moduleUuid, UUID quizUuid) {
        return service.findQuizByUuid(quizUuid);
    }

    @Override
    public QuizResponse update(UUID moduleUuid, UUID quizUuid, QuizRequest.Update request) {
        return service.updateQuiz(quizUuid, request);
    }

    @Override
    public QuizResponse updateStatus(UUID moduleUuid, UUID quizUuid, QuizRequest.UpdateStatus request) {
        return service.updateQuizStatus(quizUuid, request);
    }

    @Override
    public void delete(UUID moduleUuid, UUID quizUuid) {
        service.deleteQuiz(quizUuid);
    }

    @Override
    public QuizResponse duplicate(UUID moduleUuid, UUID quizUuid, UUID targetModuleUuid) {
        return service.duplicateQuiz(quizUuid, targetModuleUuid != null ? targetModuleUuid : moduleUuid);
    }

    @Override
    public QuizResponse reopen(UUID moduleUuid, UUID quizUuid, QuizRequest.Reopen request) {
        return service.reopenQuiz(quizUuid, request);
    }
}

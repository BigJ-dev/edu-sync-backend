package com.edusync.api.course.quiz.common.service;

import com.edusync.api.course.quiz.common.dto.QuizRequest;
import com.edusync.api.course.quiz.common.dto.QuizResponse;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.course.quiz.common.model.Quiz;

import java.util.List;
import java.util.UUID;

public interface QuizService {

    QuizResponse createQuiz(UUID moduleUuid, QuizRequest.Create request);

    List<QuizResponse> findAllQuizzesByModule(UUID moduleUuid, QuizStatus status, String search);

    QuizResponse findQuizByUuid(UUID quizUuid);

    QuizResponse updateQuiz(UUID quizUuid, QuizRequest.Update request);

    QuizResponse updateQuizStatus(UUID quizUuid, QuizRequest.UpdateStatus request);

    Quiz findQuizEntityByUuid(UUID uuid);
}

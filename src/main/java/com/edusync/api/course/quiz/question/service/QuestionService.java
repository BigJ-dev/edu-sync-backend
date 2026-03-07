package com.edusync.api.course.quiz.question.service;

import com.edusync.api.course.quiz.question.dto.OptionResponse;
import com.edusync.api.course.quiz.question.dto.QuestionRequest;
import com.edusync.api.course.quiz.question.dto.QuestionResponse;
import com.edusync.api.course.quiz.question.model.QuizQuestion;

import java.util.List;
import java.util.UUID;

public interface QuestionService {

    QuestionResponse addQuizQuestion(UUID quizUuid, QuestionRequest.Create request);

    List<QuestionResponse> findAllQuestionsByQuiz(UUID quizUuid);

    QuestionResponse findQuizQuestionByUuid(UUID questionUuid);

    QuestionResponse updateQuizQuestion(UUID questionUuid, QuestionRequest.Update request);

    void deleteQuizQuestion(UUID questionUuid);

    List<OptionResponse> replaceQuizQuestionOptions(UUID questionUuid, List<QuestionRequest.OptionCreate> options);

    QuizQuestion findQuizQuestionEntityByUuid(UUID uuid);
}

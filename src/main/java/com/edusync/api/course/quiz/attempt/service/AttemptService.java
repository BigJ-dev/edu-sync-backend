package com.edusync.api.course.quiz.attempt.service;

import com.edusync.api.course.quiz.attempt.dto.AnswerResponse;
import com.edusync.api.course.quiz.attempt.dto.AttemptRequest;
import com.edusync.api.course.quiz.attempt.dto.AttemptResponse;
import com.edusync.api.course.quiz.attempt.model.QuizAttempt;

import java.util.List;
import java.util.UUID;

public interface AttemptService {

    AttemptResponse startQuizAttempt(UUID quizUuid, AttemptRequest.Start request);

    AnswerResponse submitQuizAnswer(UUID attemptUuid, AttemptRequest.SubmitAnswer request);

    AttemptResponse completeQuizAttempt(UUID attemptUuid);

    AttemptResponse findQuizAttemptByUuid(UUID attemptUuid);

    List<AnswerResponse> findQuizAttemptAnswers(UUID attemptUuid);

    List<AttemptResponse> findQuizAttemptsByQuizAndStudent(UUID quizUuid, UUID studentUuid);

    QuizAttempt findQuizAttemptEntityByUuid(UUID uuid);
}

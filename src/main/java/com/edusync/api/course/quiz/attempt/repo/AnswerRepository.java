package com.edusync.api.course.quiz.attempt.repo;

import com.edusync.api.course.quiz.attempt.model.QuizAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<QuizAnswer, Long> {

    List<QuizAnswer> findByAttemptId(Long attemptId);
}

package com.edusync.api.course.quiz.attempt.repo;

import com.edusync.api.course.quiz.attempt.model.QuizAnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerOptionRepository extends JpaRepository<QuizAnswerOption, Long> {

    List<QuizAnswerOption> findByAnswerId(Long answerId);
}

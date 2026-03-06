package com.edusync.api.course.quiz.question.repo;

import com.edusync.api.course.quiz.question.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<QuizQuestion, Long> {

    Optional<QuizQuestion> findByUuid(UUID uuid);

    List<QuizQuestion> findByQuizIdOrderBySortOrderAsc(Long quizId);
}

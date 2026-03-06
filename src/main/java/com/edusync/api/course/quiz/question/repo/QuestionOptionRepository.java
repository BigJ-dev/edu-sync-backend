package com.edusync.api.course.quiz.question.repo;

import com.edusync.api.course.quiz.question.model.QuizQuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuestionOptionRepository extends JpaRepository<QuizQuestionOption, Long> {

    Optional<QuizQuestionOption> findByUuid(UUID uuid);

    List<QuizQuestionOption> findByQuestionIdOrderBySortOrderAsc(Long questionId);

    void deleteByQuestionId(Long questionId);
}

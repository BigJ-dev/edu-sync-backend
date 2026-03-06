package com.edusync.api.course.quiz.attempt.repo;

import com.edusync.api.course.quiz.attempt.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttemptRepository extends JpaRepository<QuizAttempt, Long> {

    Optional<QuizAttempt> findByUuid(UUID uuid);

    List<QuizAttempt> findByQuizIdAndStudentId(Long quizId, Long studentId);

    long countByQuizIdAndStudentId(Long quizId, Long studentId);
}

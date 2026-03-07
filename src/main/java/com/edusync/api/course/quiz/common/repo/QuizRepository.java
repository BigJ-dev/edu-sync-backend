package com.edusync.api.course.quiz.common.repo;

import com.edusync.api.course.quiz.common.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, Long>, JpaSpecificationExecutor<Quiz> {

    Optional<Quiz> findByUuid(UUID uuid);

    boolean existsByModuleIdAndTitle(Long moduleId, String title);

    List<Quiz> findByModuleCourseId(Long courseId);
}

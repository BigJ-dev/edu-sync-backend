package com.edusync.api.course.assessment.rubric.repo;

import com.edusync.api.course.assessment.rubric.model.RubricCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RubricCriteriaRepository extends JpaRepository<RubricCriteria, Long> {

    Optional<RubricCriteria> findByUuid(UUID uuid);

    List<RubricCriteria> findByAssessmentIdOrderBySortOrder(Long assessmentId);

    boolean existsByAssessmentIdAndSortOrder(Long assessmentId, int sortOrder);
}

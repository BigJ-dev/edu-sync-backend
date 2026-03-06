package com.edusync.api.course.assessment.grade.repo;

import com.edusync.api.course.assessment.grade.model.RubricGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RubricGradeRepository extends JpaRepository<RubricGrade, Long> {

    List<RubricGrade> findBySubmissionId(Long submissionId);

    Optional<RubricGrade> findBySubmissionIdAndCriteriaId(Long submissionId, Long criteriaId);

    boolean existsBySubmissionIdAndCriteriaId(Long submissionId, Long criteriaId);
}

package com.edusync.api.course.assessment.submission.repo;

import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<AssessmentSubmission, Long>, JpaSpecificationExecutor<AssessmentSubmission> {

    Optional<AssessmentSubmission> findByUuid(UUID uuid);

    Optional<AssessmentSubmission> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    boolean existsByAssessmentIdAndStudentId(Long assessmentId, Long studentId);
}

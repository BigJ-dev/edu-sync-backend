package com.edusync.api.course.assessment.submission.repo;

import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<AssessmentSubmission, Long>, JpaSpecificationExecutor<AssessmentSubmission> {

    Optional<AssessmentSubmission> findByUuid(UUID uuid);

    Optional<AssessmentSubmission> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    boolean existsByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    List<AssessmentSubmission> findByStudentId(Long studentId);

    @Query("SELECT COUNT(s) FROM AssessmentSubmission s WHERE s.assessment.module.course.id = :courseId AND s.status = :status")
    long countByCourseIdAndStatus(Long courseId, SubmissionStatus status);

    @Query("SELECT COUNT(a) FROM Assessment a WHERE a.module.course.id = :courseId AND a.dueDate > CURRENT_TIMESTAMP AND NOT EXISTS (SELECT s FROM AssessmentSubmission s WHERE s.assessment = a AND s.student.id = :studentId)")
    long countPendingByStudentIdAndCourseId(Long studentId, Long courseId);

    List<AssessmentSubmission> findByAssessmentIdInAndStudentId(List<Long> assessmentIds, Long studentId);
}

package com.edusync.api.course.assessment.submission.service;

import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.assessment.submission.enums.SubmissionField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SubmissionSpec {

    public static Specification<AssessmentSubmission> hasAssessmentId(Long assessmentId) {
        return (root, query, cb) -> cb.equal(root.get(ASSESSMENT_ID.getName()), assessmentId);
    }

    public static Specification<AssessmentSubmission> hasStatus(SubmissionStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<AssessmentSubmission> isLate(Boolean late) {
        return late == null ? null : (root, query, cb) -> cb.equal(root.get(IS_LATE.getName()), late);
    }
}

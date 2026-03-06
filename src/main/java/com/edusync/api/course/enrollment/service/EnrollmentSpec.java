package com.edusync.api.course.enrollment.service;

import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.enrollment.enums.EnrollmentField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnrollmentSpec {

    public static Specification<CourseEnrollment> hasCourseId(Long courseId) {
        return (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<CourseEnrollment> hasStudentId(Long studentId) {
        return studentId == null ? null : (root, query, cb) -> cb.equal(root.get(STUDENT_ID.getName()), studentId);
    }

    public static Specification<CourseEnrollment> hasStatus(EnrollmentStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<CourseEnrollment> isBlocked(Boolean blocked) {
        return blocked == null ? null : (root, query, cb) -> cb.equal(root.get(BLOCKED.getName()), blocked);
    }
}

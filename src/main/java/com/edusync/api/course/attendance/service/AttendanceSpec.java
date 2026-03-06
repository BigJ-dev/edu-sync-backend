package com.edusync.api.course.attendance.service;

import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.model.AttendanceRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.attendance.enums.AttendanceField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AttendanceSpec {

    public static Specification<AttendanceRecord> hasSessionId(Long sessionId) {
        return (root, query, cb) -> cb.equal(root.get(CLASS_SESSION_ID.getName()), sessionId);
    }

    public static Specification<AttendanceRecord> hasCourseId(Long courseId) {
        return courseId == null ? null : (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<AttendanceRecord> hasModuleId(Long moduleId) {
        return moduleId == null ? null : (root, query, cb) -> cb.equal(root.get(MODULE_ID.getName()), moduleId);
    }

    public static Specification<AttendanceRecord> hasStatus(AttendanceStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(ATTENDANCE_STATUS.getName()), status);
    }
}

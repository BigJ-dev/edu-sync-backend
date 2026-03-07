package com.edusync.api.course.support.service;

import com.edusync.api.course.support.enums.ThreadPriority;
import com.edusync.api.course.support.enums.ThreadStatus;
import com.edusync.api.course.support.model.SupportThread;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.support.enums.ThreadField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SupportThreadSpec {

    public static Specification<SupportThread> hasCourseId(Long courseId) {
        return (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<SupportThread> hasStudentId(Long studentId) {
        return studentId == null ? null : (root, query, cb) -> cb.equal(root.get(STUDENT.getName()).get("id"), studentId);
    }

    public static Specification<SupportThread> hasStatus(ThreadStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<SupportThread> hasPriority(ThreadPriority priority) {
        return priority == null ? null : (root, query, cb) -> cb.equal(root.get(PRIORITY.getName()), priority);
    }

    public static Specification<SupportThread> isEscalated(Boolean escalated) {
        return escalated == null ? null : (root, query, cb) -> cb.equal(root.get(IS_ESCALATED.getName()), escalated);
    }

    public static Specification<SupportThread> searchBySubject(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(SUBJECT.getName())), pattern);
    }
}

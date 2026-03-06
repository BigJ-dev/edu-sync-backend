package com.edusync.api.course.messaging.service;

import com.edusync.api.course.messaging.enums.ThreadPriority;
import com.edusync.api.course.messaging.enums.ThreadStatus;
import com.edusync.api.course.messaging.model.MessageThread;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.messaging.enums.ThreadField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadSpec {

    public static Specification<MessageThread> hasCourseId(Long courseId) {
        return (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<MessageThread> hasStudentId(Long studentId) {
        return studentId == null ? null : (root, query, cb) -> cb.equal(root.get(STUDENT.getName()).get("id"), studentId);
    }

    public static Specification<MessageThread> hasStatus(ThreadStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<MessageThread> hasPriority(ThreadPriority priority) {
        return priority == null ? null : (root, query, cb) -> cb.equal(root.get(PRIORITY.getName()), priority);
    }

    public static Specification<MessageThread> isEscalated(Boolean escalated) {
        return escalated == null ? null : (root, query, cb) -> cb.equal(root.get(IS_ESCALATED.getName()), escalated);
    }

    public static Specification<MessageThread> searchBySubject(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(SUBJECT.getName())), pattern);
    }
}

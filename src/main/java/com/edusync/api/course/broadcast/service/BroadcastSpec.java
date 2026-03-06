package com.edusync.api.course.broadcast.service;

import com.edusync.api.course.broadcast.enums.BroadcastPriority;
import com.edusync.api.course.broadcast.enums.BroadcastTarget;
import com.edusync.api.course.broadcast.model.BroadcastMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.broadcast.enums.BroadcastField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BroadcastSpec {

    public static Specification<BroadcastMessage> hasCourseId(Long courseId) {
        return courseId == null ? null : (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<BroadcastMessage> hasTargetType(BroadcastTarget targetType) {
        return targetType == null ? null : (root, query, cb) -> cb.equal(root.get(TARGET_TYPE.getName()), targetType);
    }

    public static Specification<BroadcastMessage> hasPriority(BroadcastPriority priority) {
        return priority == null ? null : (root, query, cb) -> cb.equal(root.get(PRIORITY.getName()), priority);
    }

    public static Specification<BroadcastMessage> searchByTitle(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(TITLE.getName())), pattern);
    }
}

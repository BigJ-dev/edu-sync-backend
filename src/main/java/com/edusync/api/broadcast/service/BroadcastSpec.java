package com.edusync.api.broadcast.service;

import com.edusync.api.broadcast.enums.BroadcastPriority;
import com.edusync.api.broadcast.enums.BroadcastTarget;
import com.edusync.api.broadcast.model.BroadcastMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.edusync.api.broadcast.enums.BroadcastField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BroadcastSpec {

    public static Specification<BroadcastMessage> hasCourseId(Long courseId) {
        return Objects.isNull(courseId) ? null : (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<BroadcastMessage> hasTargetType(BroadcastTarget targetType) {
        return Objects.isNull(targetType) ? null : (root, query, cb) -> cb.equal(root.get(TARGET_TYPE.getName()), targetType);
    }

    public static Specification<BroadcastMessage> hasPriority(BroadcastPriority priority) {
        return Objects.isNull(priority) ? null : (root, query, cb) -> cb.equal(root.get(PRIORITY.getName()), priority);
    }

    public static Specification<BroadcastMessage> searchByTitle(String search) {
        if (Objects.isNull(search) || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(TITLE.getName())), pattern);
    }
}

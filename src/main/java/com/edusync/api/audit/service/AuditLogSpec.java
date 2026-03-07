package com.edusync.api.audit.service;

import com.edusync.api.audit.enums.AuditAction;
import com.edusync.api.audit.enums.PerformerType;
import com.edusync.api.audit.model.AuditLog;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.Objects;

import static com.edusync.api.audit.enums.AuditField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditLogSpec {

    public static Specification<AuditLog> hasAction(AuditAction action) {
        return Objects.isNull(action) ? null : (root, query, cb) -> cb.equal(root.get(ACTION.getName()), action);
    }

    public static Specification<AuditLog> hasEntityType(String entityType) {
        return Objects.isNull(entityType) ? null : (root, query, cb) -> cb.equal(root.get(ENTITY_TYPE.getName()), entityType);
    }

    public static Specification<AuditLog> hasEntityId(Long entityId) {
        return Objects.isNull(entityId) ? null : (root, query, cb) -> cb.equal(root.get(ENTITY_ID.getName()), entityId);
    }

    public static Specification<AuditLog> hasPerformedByType(PerformerType performedByType) {
        return Objects.isNull(performedByType) ? null : (root, query, cb) -> cb.equal(root.get(PERFORMED_BY_TYPE.getName()), performedByType);
    }

    public static Specification<AuditLog> hasCourseId(Long courseId) {
        return Objects.isNull(courseId) ? null : (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<AuditLog> createdAfter(Instant from) {
        return Objects.isNull(from) ? null : (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(CREATED_AT.getName()), from);
    }

    public static Specification<AuditLog> createdBefore(Instant to) {
        return Objects.isNull(to) ? null : (root, query, cb) -> cb.lessThanOrEqualTo(root.get(CREATED_AT.getName()), to);
    }
}

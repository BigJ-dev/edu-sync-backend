package com.edusync.api.course.session.service;

import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import com.edusync.api.course.session.model.ClassSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.session.enums.ClassSessionField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassSessionSpec {

    public static Specification<ClassSession> hasModuleId(Long moduleId) {
        return (root, query, cb) -> cb.equal(root.get(MODULE_ID.getName()), moduleId);
    }

    public static Specification<ClassSession> hasStatus(ClassSessionStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<ClassSession> hasSessionType(ClassSessionType sessionType) {
        return sessionType == null ? null : (root, query, cb) -> cb.equal(root.get(SESSION_TYPE.getName()), sessionType);
    }

    public static Specification<ClassSession> searchByTitle(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(TITLE.getName())), pattern);
    }
}

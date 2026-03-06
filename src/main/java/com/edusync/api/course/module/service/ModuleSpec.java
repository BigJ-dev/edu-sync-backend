package com.edusync.api.course.module.service;

import com.edusync.api.course.module.enums.ModuleStatus;
import com.edusync.api.course.module.model.CourseModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.module.enums.ModuleField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModuleSpec {

    public static Specification<CourseModule> hasCourseId(Long courseId) {
        return (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<CourseModule> hasStatus(ModuleStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<CourseModule> searchByTitle(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(TITLE.getName())), pattern);
    }
}

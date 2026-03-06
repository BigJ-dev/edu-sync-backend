package com.edusync.api.course.group.service;

import com.edusync.api.course.group.model.CourseGroup;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.group.enums.GroupField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GroupSpec {

    public static Specification<CourseGroup> hasCourseId(Long courseId) {
        return (root, query, cb) -> cb.equal(root.get(COURSE_ID.getName()), courseId);
    }

    public static Specification<CourseGroup> searchByName(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(NAME.getName())), pattern);
    }
}

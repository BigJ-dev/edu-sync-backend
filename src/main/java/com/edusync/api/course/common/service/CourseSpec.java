package com.edusync.api.course.common.service;

import com.edusync.api.course.common.enums.CourseStatus;
import com.edusync.api.course.common.model.Course;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.common.enums.CourseField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CourseSpec {

    public static Specification<Course> hasStatus(CourseStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<Course> hasLecturerId(Long lecturerId) {
        return lecturerId == null ? null : (root, query, cb) -> cb.equal(root.get(LECTURER_ID.getName()), lecturerId);
    }

    public static Specification<Course> searchByCodeOrTitle(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get(CODE.getName())), pattern),
                cb.like(cb.lower(root.get(TITLE.getName())), pattern)
        );
    }
}

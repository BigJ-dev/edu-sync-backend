package com.edusync.api.actor.student.service;

import com.edusync.api.actor.student.model.Student;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.actor.student.enums.StudentField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StudentSpec {

    public static Specification<Student> isActive(Boolean active) {
        return active == null ? null : (root, query, cb) -> cb.equal(root.get(ACTIVE.getName()), active);
    }

    public static Specification<Student> searchByNameEmailOrNumber(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get(FIRST_NAME.getName())), pattern),
                cb.like(cb.lower(root.get(LAST_NAME.getName())), pattern),
                cb.like(cb.lower(root.get(EMAIL.getName())), pattern),
                cb.like(cb.lower(root.get(STUDENT_NUMBER.getName())), pattern)
        );
    }
}

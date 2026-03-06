package com.edusync.api.course.quiz.common.service;

import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.course.quiz.common.model.Quiz;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.quiz.common.enums.QuizField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuizSpec {

    public static Specification<Quiz> hasModuleId(Long moduleId) {
        return (root, query, cb) -> cb.equal(root.get(MODULE_ID.getName()), moduleId);
    }

    public static Specification<Quiz> hasStatus(QuizStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<Quiz> searchByTitle(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(TITLE.getName())), pattern);
    }
}

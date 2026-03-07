package com.edusync.api.course.category.service;

import com.edusync.api.course.category.model.CourseCategory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.edusync.api.course.category.enums.CategoryField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategorySpec {

    public static Specification<CourseCategory> hasParentId(Long parentId) {
        return Objects.isNull(parentId) ? null : (root, query, cb) -> cb.equal(root.get(PARENT.getName()).get("id"), parentId);
    }

    public static Specification<CourseCategory> isActive(Boolean active) {
        return Objects.isNull(active) ? null : (root, query, cb) -> cb.equal(root.get(ACTIVE.getName()), active);
    }

    public static Specification<CourseCategory> searchByName(String search) {
        if (Objects.isNull(search) || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(NAME.getName())), pattern);
    }
}

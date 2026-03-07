package com.edusync.api.course.material.service;

import com.edusync.api.course.material.enums.MaterialType;
import com.edusync.api.course.material.model.StudyMaterial;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static com.edusync.api.course.material.enums.MaterialField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MaterialSpec {

    public static Specification<StudyMaterial> hasModuleId(Long moduleId) {
        return (root, query, cb) -> cb.equal(root.get(MODULE_ID.getName()), moduleId);
    }

    public static Specification<StudyMaterial> hasMaterialType(MaterialType type) {
        return Objects.isNull(type) ? null : (root, query, cb) -> cb.equal(root.get(MATERIAL_TYPE.getName()), type);
    }

    public static Specification<StudyMaterial> isVisibleToStudents(Boolean visible) {
        return Objects.isNull(visible) ? null : (root, query, cb) -> cb.equal(root.get(VISIBLE_TO_STUDENTS.getName()), visible);
    }

    public static Specification<StudyMaterial> searchByTitle(String search) {
        if (Objects.isNull(search) || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(TITLE.getName())), pattern);
    }
}

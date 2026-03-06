package com.edusync.api.course.assessment.common.service;

import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.common.enums.DeliveryMode;
import com.edusync.api.course.assessment.common.model.Assessment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.course.assessment.common.enums.AssessmentField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssessmentSpec {

    public static Specification<Assessment> hasModuleId(Long moduleId) {
        return (root, query, cb) -> cb.equal(root.get(MODULE_ID.getName()), moduleId);
    }

    public static Specification<Assessment> hasStatus(AssessmentStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get(STATUS.getName()), status);
    }

    public static Specification<Assessment> hasAssessmentType(AssessmentType type) {
        return type == null ? null : (root, query, cb) -> cb.equal(root.get(ASSESSMENT_TYPE.getName()), type);
    }

    public static Specification<Assessment> hasDeliveryMode(DeliveryMode mode) {
        return mode == null ? null : (root, query, cb) -> cb.equal(root.get(DELIVERY_MODE.getName()), mode);
    }

    public static Specification<Assessment> searchByTitle(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(TITLE.getName())), pattern);
    }
}

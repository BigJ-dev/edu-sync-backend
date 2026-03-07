package com.edusync.api.admission.service;

import com.edusync.api.admission.enums.ApplicationStatus;
import com.edusync.api.admission.model.StudentApplication;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationSpec {

    public static Specification<StudentApplication> hasStatus(ApplicationStatus status) {
        return Objects.isNull(status) ? null : (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<StudentApplication> searchByNameOrEmail(String search) {
        if (Objects.isNull(search) || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("firstName")), pattern),
                cb.like(cb.lower(root.get("lastName")), pattern),
                cb.like(cb.lower(root.get("email")), pattern)
        );
    }
}

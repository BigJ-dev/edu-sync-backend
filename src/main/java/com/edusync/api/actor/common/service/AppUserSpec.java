package com.edusync.api.actor.common.service;

import com.edusync.api.actor.common.enums.UserRole;
import com.edusync.api.actor.common.model.AppUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.actor.common.enums.AppUserField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AppUserSpec {

    public static Specification<AppUser> hasRole(UserRole role) {
        return (root, query, cb) -> cb.equal(root.get(ROLE.getName()), role);
    }

    public static Specification<AppUser> isActive(Boolean active) {
        return active == null ? null : (root, query, cb) -> cb.equal(root.get(ACTIVE.getName()), active);
    }

    public static Specification<AppUser> searchByNameOrEmail(String search) {
        if (search == null || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get(FIRST_NAME.getName())), pattern),
                cb.like(cb.lower(root.get(LAST_NAME.getName())), pattern),
                cb.like(cb.lower(root.get(EMAIL.getName())), pattern)
        );
    }
}

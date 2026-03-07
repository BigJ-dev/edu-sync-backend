package com.edusync.api.announcement.service;

import com.edusync.api.announcement.enums.AnnouncementCategory;
import com.edusync.api.announcement.enums.AnnouncementStatus;
import com.edusync.api.announcement.model.Announcement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AnnouncementSpec {

    public static Specification<Announcement> hasCategory(AnnouncementCategory category) {
        return Objects.isNull(category) ? null : (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    public static Specification<Announcement> hasStatus(AnnouncementStatus status) {
        return Objects.isNull(status) ? null : (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Announcement> isPinned(Boolean pinned) {
        return Objects.isNull(pinned) ? null : (root, query, cb) -> cb.equal(root.get("pinned"), pinned);
    }

    public static Specification<Announcement> searchByTitle(String search) {
        if (Objects.isNull(search) || search.isBlank()) return null;
        var pattern = "%" + search.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), pattern);
    }
}

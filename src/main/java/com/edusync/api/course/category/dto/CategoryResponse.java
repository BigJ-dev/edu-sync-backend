package com.edusync.api.course.category.dto;

import com.edusync.api.course.category.model.CourseCategory;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(
        UUID uuid,
        String name,
        String description,
        UUID parentUuid,
        int sortOrder,
        String iconName,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public static CategoryResponse from(CourseCategory category) {
        return new CategoryResponse(
                category.getUuid(),
                category.getName(),
                category.getDescription(),
                category.getParent() != null ? category.getParent().getUuid() : null,
                category.getSortOrder(),
                category.getIconName(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}

package com.edusync.api.course.group.dto;

import com.edusync.api.course.group.model.CourseGroup;

import java.time.Instant;
import java.util.UUID;

public record GroupResponse(
        UUID uuid,
        UUID courseUuid,
        UUID moduleUuid,
        String name,
        String description,
        Integer maxMembers,
        int memberCount,
        UUID createdByUuid,
        Instant createdAt,
        Instant updatedAt
) {
    public static GroupResponse from(CourseGroup group, int memberCount) {
        return new GroupResponse(
                group.getUuid(),
                group.getCourse().getUuid(),
                group.getModule() != null ? group.getModule().getUuid() : null,
                group.getName(),
                group.getDescription(),
                group.getMaxMembers(),
                memberCount,
                group.getCreatedBy().getUuid(),
                group.getCreatedAt(),
                group.getUpdatedAt()
        );
    }
}

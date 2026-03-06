package com.edusync.api.course.module.dto;

import com.edusync.api.course.module.enums.ModuleStatus;
import com.edusync.api.course.module.model.CourseModule;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ModuleResponse(
        UUID uuid,
        UUID courseUuid,
        String title,
        String description,
        int sortOrder,
        ModuleStatus status,
        LocalDate startDate,
        LocalDate endDate,
        Instant createdAt,
        Instant updatedAt
) {
    public static ModuleResponse from(CourseModule module) {
        return new ModuleResponse(
                module.getUuid(),
                module.getCourse().getUuid(),
                module.getTitle(),
                module.getDescription(),
                module.getSortOrder(),
                module.getStatus(),
                module.getStartDate(),
                module.getEndDate(),
                module.getCreatedAt(),
                module.getUpdatedAt()
        );
    }
}

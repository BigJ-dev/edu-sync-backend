package com.edusync.api.course.material.dto;

import com.edusync.api.course.material.enums.MaterialType;
import com.edusync.api.course.material.model.StudyMaterial;

import java.time.Instant;
import java.util.UUID;

public record MaterialResponse(
        UUID uuid,
        UUID moduleUuid,
        UUID classSessionUuid,
        UUID uploadedByUuid,
        String title,
        String description,
        MaterialType materialType,
        String s3Key,
        String externalUrl,
        String fileName,
        Long fileSizeBytes,
        String mimeType,
        int sortOrder,
        boolean visibleToStudents,
        Instant createdAt,
        Instant updatedAt
) {
    public static MaterialResponse from(StudyMaterial material) {
        return new MaterialResponse(
                material.getUuid(),
                material.getModule().getUuid(),
                material.getClassSession() != null ? material.getClassSession().getUuid() : null,
                material.getUploadedBy().getUuid(),
                material.getTitle(),
                material.getDescription(),
                material.getMaterialType(),
                material.getS3Key(),
                material.getExternalUrl(),
                material.getFileName(),
                material.getFileSizeBytes(),
                material.getMimeType(),
                material.getSortOrder(),
                material.isVisibleToStudents(),
                material.getCreatedAt(),
                material.getUpdatedAt()
        );
    }
}

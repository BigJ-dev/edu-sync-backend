package com.edusync.api.audit.dto;

import com.edusync.api.audit.enums.AuditAction;
import com.edusync.api.audit.enums.PerformerType;
import com.edusync.api.audit.model.AuditLog;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResponse(
        Long id,
        AuditAction action,
        String entityType,
        Long entityId,
        UUID entityUuid,
        PerformerType performedByType,
        Long performedByStudentId,
        Long performedByUserId,
        String fieldName,
        String oldValue,
        String newValue,
        String description,
        Long courseId,
        String ipAddress,
        String userAgent,
        Instant createdAt
) {
    public static AuditLogResponse from(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getAction(),
                log.getEntityType(),
                log.getEntityId(),
                log.getEntityUuid(),
                log.getPerformedByType(),
                log.getPerformedByStudentId(),
                log.getPerformedByUserId(),
                log.getFieldName(),
                log.getOldValue(),
                log.getNewValue(),
                log.getDescription(),
                log.getCourseId(),
                log.getIpAddress(),
                log.getUserAgent(),
                log.getCreatedAt()
        );
    }
}

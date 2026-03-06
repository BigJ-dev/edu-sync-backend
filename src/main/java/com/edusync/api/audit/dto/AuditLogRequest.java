package com.edusync.api.audit.dto;

import com.edusync.api.audit.enums.AuditAction;
import com.edusync.api.audit.enums.PerformerType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.UUID;

public sealed interface AuditLogRequest {

    @Schema(name = "AuditLogCreate")
    record Create(
            @NotNull(message = "Action is required")
            AuditAction action,

            @NotBlank(message = "Entity type is required")
            @Size(max = 50, message = "Entity type must not exceed 50 characters")
            String entityType,

            @NotNull(message = "Entity ID is required")
            Long entityId,

            UUID entityUuid,

            PerformerType performedByType,

            Long performedByStudentId,

            Long performedByUserId,

            @Size(max = 100, message = "Field name must not exceed 100 characters")
            String fieldName,

            String oldValue,

            String newValue,

            String description,

            Long courseId,

            @Size(max = 45, message = "IP address must not exceed 45 characters")
            String ipAddress,

            @Size(max = 500, message = "User agent must not exceed 500 characters")
            String userAgent
    ) implements AuditLogRequest {}
}

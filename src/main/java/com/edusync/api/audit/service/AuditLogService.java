package com.edusync.api.audit.service;

import com.edusync.api.audit.dto.AuditLogRequest;
import com.edusync.api.audit.dto.AuditLogResponse;
import com.edusync.api.audit.enums.AuditAction;
import com.edusync.api.audit.enums.PerformerType;

import java.time.Instant;
import java.util.List;

public interface AuditLogService {

    AuditLogResponse createAuditLog(AuditLogRequest.Create request);

    List<AuditLogResponse> findAllAuditLogs(AuditAction action, String entityType, Long entityId,
                                   PerformerType performedByType, Long courseId,
                                   Instant from, Instant to);

    AuditLogResponse findAuditLogById(Long id);
}

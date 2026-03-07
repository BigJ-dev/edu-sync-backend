package com.edusync.api.audit.service;

import com.edusync.api.audit.dto.AuditLogRequest;
import com.edusync.api.audit.dto.AuditLogResponse;

import java.util.List;

public interface AuditLogService {

    AuditLogResponse createAuditLog(AuditLogRequest.Create request);

    List<AuditLogResponse> findAllAuditLogs(AuditLogRequest.Filter filter);

    AuditLogResponse findAuditLogById(Long id);
}

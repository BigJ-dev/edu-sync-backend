package com.edusync.api.audit.controller;

import com.edusync.api.audit.controller.api.AuditLogApi;
import com.edusync.api.audit.dto.AuditLogRequest;
import com.edusync.api.audit.dto.AuditLogResponse;
import com.edusync.api.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AuditLogController implements AuditLogApi {

    private final AuditLogService service;

    @Override
    public AuditLogResponse createAuditLog(AuditLogRequest.Create request) {
        return service.createAuditLog(request);
    }

    @Override
    public List<AuditLogResponse> findAllAuditLogs(AuditLogRequest.Filter filter) {
        return service.findAllAuditLogs(filter);
    }

    @Override
    public AuditLogResponse findAuditLogById(Long id) {
        return service.findAuditLogById(id);
    }
}

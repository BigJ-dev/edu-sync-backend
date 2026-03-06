package com.edusync.api.audit.controller;

import com.edusync.api.audit.controller.api.AuditLogApi;
import com.edusync.api.audit.dto.AuditLogRequest;
import com.edusync.api.audit.dto.AuditLogResponse;
import com.edusync.api.audit.enums.AuditAction;
import com.edusync.api.audit.enums.PerformerType;
import com.edusync.api.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class AuditLogController implements AuditLogApi {

    private final AuditLogService service;

    @Override
    public AuditLogResponse create(AuditLogRequest.Create request) {
        return service.log(request);
    }

    @Override
    public List<AuditLogResponse> findAll(AuditAction action, String entityType, Long entityId,
                                          PerformerType performedByType, Long courseId,
                                          Instant from, Instant to) {
        return service.findAll(action, entityType, entityId, performedByType, courseId, from, to);
    }

    @Override
    public AuditLogResponse findById(Long id) {
        return service.findById(id);
    }
}

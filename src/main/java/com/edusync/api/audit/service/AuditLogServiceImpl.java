package com.edusync.api.audit.service;

import com.edusync.api.audit.dto.AuditLogRequest;
import com.edusync.api.audit.dto.AuditLogResponse;
import com.edusync.api.audit.enums.AuditAction;
import com.edusync.api.audit.enums.PerformerType;
import com.edusync.api.audit.model.AuditLog;
import com.edusync.api.audit.repo.AuditLogRepository;
import com.edusync.api.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;

    @Override
    public AuditLogResponse createAuditLog(AuditLogRequest.Create request) {
        var auditLog = AuditLog.builder()
                .action(request.action())
                .entityType(request.entityType())
                .entityId(request.entityId())
                .entityUuid(request.entityUuid())
                .performedByType(request.performedByType())
                .performedByStudentId(request.performedByStudentId())
                .performedByUserId(request.performedByUserId())
                .fieldName(request.fieldName())
                .oldValue(request.oldValue())
                .newValue(request.newValue())
                .description(request.description())
                .courseId(request.courseId())
                .ipAddress(request.ipAddress())
                .userAgent(request.userAgent())
                .build();

        return AuditLogResponse.from(repository.save(auditLog));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> findAllAuditLogs(AuditAction action, String entityType, Long entityId,
                                          PerformerType performedByType, Long courseId,
                                          Instant from, Instant to) {
        var spec = Specification.where(AuditLogSpec.hasAction(action))
                .and(AuditLogSpec.hasEntityType(entityType))
                .and(AuditLogSpec.hasEntityId(entityId))
                .and(AuditLogSpec.hasPerformedByType(performedByType))
                .and(AuditLogSpec.hasCourseId(courseId))
                .and(AuditLogSpec.createdAfter(from))
                .and(AuditLogSpec.createdBefore(to));

        return repository.findAll(spec).stream().map(AuditLogResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogResponse findAuditLogById(Long id) {
        return AuditLogResponse.from(repository.findById(id)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Audit log was not found")));
    }
}

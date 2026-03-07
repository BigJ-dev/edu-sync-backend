package com.edusync.api.audit.controller.api;

import com.edusync.api.audit.dto.AuditLogRequest;
import com.edusync.api.audit.dto.AuditLogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Audit Log", description = "Endpoints for managing audit log entries. Tracks all significant actions performed within the system.")
@RequestMapping("/audit-logs")
public interface AuditLogApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new audit log entry",
            description = "Records a new audit log entry capturing a system action, including who performed it, " +
                    "what entity was affected, and any field-level changes."
    )
    @ApiResponse(responseCode = "201", description = "Audit log entry created successfully")
    AuditLogResponse createAuditLog(@Valid @RequestBody AuditLogRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List audit log entries",
            description = "Returns audit log entries with optional filters for action type, entity, performer, " +
                    "course, and date range."
    )
    @ApiResponse(responseCode = "200", description = "Audit log entries retrieved successfully")
    List<AuditLogResponse> findAllAuditLogs(AuditLogRequest.Filter filter);

    @GetMapping("/{id}")
    @Operation(
            summary = "Get audit log entry by ID",
            description = "Retrieves a single audit log entry by its unique identifier."
    )
    @ApiResponse(responseCode = "200", description = "Audit log entry retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Audit log entry not found")
    AuditLogResponse findAuditLogById(@PathVariable Long id);
}

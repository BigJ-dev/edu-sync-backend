package com.edusync.api.course.certificate.dto;

import com.edusync.api.course.certificate.enums.CertificateStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public sealed interface CertificateRequest {

    @Schema(name = "CertificateIssue")
    record Issue(
            @NotNull(message = "Course UUID is required")
            UUID courseUuid,

            @NotNull(message = "Student UUID is required")
            UUID studentUuid,

            @NotNull(message = "Issued by UUID is required")
            UUID issuedByUuid,

            @NotNull(message = "Completion date is required")
            LocalDate completionDate,

            BigDecimal finalGrade,

            BigDecimal finalAttendancePct,

            @Size(max = 500, message = "S3 key must not exceed 500 characters")
            String s3Key
    ) implements CertificateRequest {}

    @Schema(name = "CertificateRevoke")
    record Revoke(
            @NotBlank(message = "Revocation reason is required")
            @Size(max = 500, message = "Revocation reason must not exceed 500 characters")
            String revocationReason
    ) implements CertificateRequest {}

    @Schema(name = "CertificateFilter")
    record Filter(
            UUID courseUuid,
            CertificateStatus status,
            String search
    ) implements CertificateRequest {}
}

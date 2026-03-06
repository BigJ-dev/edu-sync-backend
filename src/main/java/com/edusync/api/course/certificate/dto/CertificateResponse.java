package com.edusync.api.course.certificate.dto;

import com.edusync.api.course.certificate.enums.CertificateStatus;
import com.edusync.api.course.certificate.model.Certificate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CertificateResponse(
        UUID uuid,
        UUID courseUuid,
        UUID studentUuid,
        UUID issuedByUuid,
        String certificateNumber,
        String verificationCode,
        BigDecimal finalGrade,
        BigDecimal finalAttendancePct,
        LocalDate completionDate,
        String s3Key,
        CertificateStatus status,
        Instant issuedAt,
        Instant revokedAt,
        String revocationReason,
        Instant createdAt,
        Instant updatedAt
) {
    public static CertificateResponse from(Certificate certificate) {
        return new CertificateResponse(
                certificate.getUuid(),
                certificate.getCourse().getUuid(),
                certificate.getStudent().getUuid(),
                certificate.getIssuedBy().getUuid(),
                certificate.getCertificateNumber(),
                certificate.getVerificationCode(),
                certificate.getFinalGrade(),
                certificate.getFinalAttendancePct(),
                certificate.getCompletionDate(),
                certificate.getS3Key(),
                certificate.getStatus(),
                certificate.getIssuedAt(),
                certificate.getRevokedAt(),
                certificate.getRevocationReason(),
                certificate.getCreatedAt(),
                certificate.getUpdatedAt()
        );
    }
}

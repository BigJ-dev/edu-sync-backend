package com.edusync.api.admission.dto;

import com.edusync.api.admission.enums.DocumentType;
import com.edusync.api.admission.model.ApplicationDocument;

import java.time.Instant;
import java.util.UUID;

public record ApplicationDocumentResponse(
        UUID uuid,
        DocumentType documentType,
        String documentName,
        String s3Key,
        Long fileSizeBytes,
        String mimeType,
        Instant createdAt
) {
    public static ApplicationDocumentResponse from(ApplicationDocument document) {
        return new ApplicationDocumentResponse(
                document.getUuid(),
                document.getDocumentType(),
                document.getDocumentName(),
                document.getS3Key(),
                document.getFileSizeBytes(),
                document.getMimeType(),
                document.getCreatedAt()
        );
    }
}

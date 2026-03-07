package com.edusync.api.admission.dto;

import com.edusync.api.admission.enums.ApplicationStatus;
import com.edusync.api.admission.enums.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public sealed interface ApplicationRequest {

    @Schema(name = "ApplicationSubmit")
    record Submit(
            @NotBlank(message = "First name is required")
            @Size(max = 100, message = "First name must not exceed 100 characters")
            String firstName,

            @NotBlank(message = "Last name is required")
            @Size(max = 100, message = "Last name must not exceed 100 characters")
            String lastName,

            @NotBlank(message = "Email is required")
            @Email(message = "Email must be valid")
            String email,

            @Size(max = 20, message = "Phone must not exceed 20 characters")
            String phone,

            @Size(max = 20, message = "ID number must not exceed 20 characters")
            String idNumber,

            LocalDate dateOfBirth,

            String address,

            @NotNull(message = "Course choices are required")
            @Size(min = 1, max = 3, message = "You must select between 1 and 3 course choices")
            List<UUID> courseChoices,

            @Valid
            List<DocumentUpload> documents
    ) implements ApplicationRequest {}

    @Schema(name = "ApplicationDocumentUpload")
    record DocumentUpload(
            @NotNull(message = "Document type is required")
            DocumentType documentType,

            @NotBlank(message = "Document name is required")
            @Size(max = 255, message = "Document name must not exceed 255 characters")
            String documentName,

            @NotBlank(message = "S3 key is required")
            @Size(max = 500, message = "S3 key must not exceed 500 characters")
            String s3Key,

            Long fileSizeBytes,

            @Size(max = 100, message = "MIME type must not exceed 100 characters")
            String mimeType
    ) implements ApplicationRequest {}

    @Schema(name = "ApplicationReview")
    record Review(
            @NotNull(message = "Reviewer UUID is required")
            UUID reviewerUuid,

            @NotNull(message = "Decision is required")
            ApplicationStatus decision,

            @Size(max = 500, message = "Rejection reason must not exceed 500 characters")
            String rejectionReason
    ) implements ApplicationRequest {}

    @Schema(name = "ApplicationFilter")
    record Filter(
            ApplicationStatus status,
            String search
    ) implements ApplicationRequest {}
}

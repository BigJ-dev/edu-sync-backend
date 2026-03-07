package com.edusync.api.admission.dto;

import com.edusync.api.admission.enums.ApplicationStatus;
import com.edusync.api.admission.model.StudentApplication;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record ApplicationResponse(
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        String phone,
        String idNumber,
        LocalDate dateOfBirth,
        String address,
        ApplicationStatus status,
        String rejectionReason,
        UUID reviewedByUuid,
        Instant reviewedAt,
        UUID studentUuid,
        String studentNumber,
        List<ApplicationCourseChoiceResponse> courseChoices,
        List<ApplicationDocumentResponse> documents,
        Instant createdAt,
        Instant updatedAt
) {
    public static ApplicationResponse from(StudentApplication application) {
        return new ApplicationResponse(
                application.getUuid(),
                application.getFirstName(),
                application.getLastName(),
                application.getEmail(),
                application.getPhone(),
                application.getIdNumber(),
                application.getDateOfBirth(),
                application.getAddress(),
                application.getStatus(),
                application.getRejectionReason(),
                Objects.nonNull(application.getReviewedBy()) ? application.getReviewedBy().getUuid() : null,
                application.getReviewedAt(),
                Objects.nonNull(application.getStudent()) ? application.getStudent().getUuid() : null,
                Objects.nonNull(application.getStudent()) ? application.getStudent().getStudentNumber() : null,
                application.getCourseChoices().stream().map(ApplicationCourseChoiceResponse::from).toList(),
                application.getDocuments().stream().map(ApplicationDocumentResponse::from).toList(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}

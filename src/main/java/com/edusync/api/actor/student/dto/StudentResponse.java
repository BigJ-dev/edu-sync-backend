package com.edusync.api.actor.student.dto;

import com.edusync.api.actor.student.model.Student;

import java.time.Instant;
import java.util.UUID;

public record StudentResponse(
        UUID uuid,
        String cognitoSub,
        String studentNumber,
        String firstName,
        String lastName,
        String email,
        String phone,
        String profileImageS3Key,
        boolean active,
        Instant blockedAt,
        String blockedReason,
        Instant lastLoginAt,
        Instant createdAt,
        Instant updatedAt
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getUuid(),
                student.getCognitoSub(),
                student.getStudentNumber(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getPhone(),
                student.getProfileImageS3Key(),
                student.isActive(),
                student.getBlockedAt(),
                student.getBlockedReason(),
                student.getLastLoginAt(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}

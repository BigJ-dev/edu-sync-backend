package com.edusync.api.course.enrollment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface EnrollmentRequest {

    record Enroll(
            @NotNull(message = _ENROLLMENT_STUDENT_REQUIRED)
            UUID studentUuid
    ) implements EnrollmentRequest {}

    record Withdraw(
            @NotBlank(message = _ENROLLMENT_WITHDRAWAL_REASON_REQUIRED)
            @Size(max = 500, message = _ENROLLMENT_WITHDRAWAL_REASON_SIZE)
            String withdrawalReason
    ) implements EnrollmentRequest {}

    record Block(
            @NotBlank(message = _BLOCKED_REASON_REQUIRED)
            @Size(max = 500, message = _BLOCKED_REASON_SIZE)
            String blockedReason
    ) implements EnrollmentRequest {}
}

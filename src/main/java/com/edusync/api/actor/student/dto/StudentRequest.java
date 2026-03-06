package com.edusync.api.actor.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface StudentRequest {

    record Create(
            @NotBlank(message = _COGNITO_SUB_REQUIRED)
            String cognitoSub,

            @NotBlank(message = _STUDENT_NUMBER_REQUIRED)
            @Size(max = 50, message = _STUDENT_NUMBER_SIZE)
            String studentNumber,

            @NotBlank(message = _FIRST_NAME_REQUIRED)
            @Size(max = 100, message = _FIRST_NAME_SIZE)
            String firstName,

            @NotBlank(message = _LAST_NAME_REQUIRED)
            @Size(max = 100, message = _LAST_NAME_SIZE)
            String lastName,

            @NotBlank(message = _EMAIL_REQUIRED)
            @Email(message = _EMAIL_INVALID)
            String email,

            @Size(max = 20, message = _PHONE_SIZE)
            String phone
    ) implements StudentRequest {}

    record Update(
            @NotBlank(message = _FIRST_NAME_REQUIRED)
            @Size(max = 100, message = _FIRST_NAME_SIZE)
            String firstName,

            @NotBlank(message = _LAST_NAME_REQUIRED)
            @Size(max = 100, message = _LAST_NAME_SIZE)
            String lastName,

            @NotBlank(message = _EMAIL_REQUIRED)
            @Email(message = _EMAIL_INVALID)
            String email,

            @Size(max = 20, message = _PHONE_SIZE)
            String phone,

            @NotBlank(message = _STUDENT_NUMBER_REQUIRED)
            @Size(max = 50, message = _STUDENT_NUMBER_SIZE)
            String studentNumber
    ) implements StudentRequest {}

    record Block(
            @NotBlank(message = _BLOCKED_REASON_REQUIRED)
            @Size(max = 500, message = _BLOCKED_REASON_SIZE)
            String blockedReason
    ) implements StudentRequest {}

    record ProfileImage(
            @NotBlank(message = _PROFILE_IMAGE_KEY_REQUIRED)
            @Size(max = 500, message = _PROFILE_IMAGE_KEY_SIZE)
            String profileImageS3Key
    ) implements StudentRequest {}
}

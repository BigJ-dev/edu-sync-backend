package com.edusync.api.actor.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface AppUserRequest {

    @Schema(name = "AppUserCreate")
    record Create(
            @NotBlank(message = _COGNITO_SUB_REQUIRED)
            String cognitoSub,

            @NotBlank(message = _EMAIL_REQUIRED)
            @Email(message = _EMAIL_INVALID)
            String email,

            @NotBlank(message = _FIRST_NAME_REQUIRED)
            @Size(max = 100, message = _FIRST_NAME_SIZE)
            String firstName,

            @NotBlank(message = _LAST_NAME_REQUIRED)
            @Size(max = 100, message = _LAST_NAME_SIZE)
            String lastName
    ) implements AppUserRequest {}

    @Schema(name = "AppUserUpdate")
    record Update(
            @NotBlank(message = _FIRST_NAME_REQUIRED)
            @Size(max = 100, message = _FIRST_NAME_SIZE)
            String firstName,

            @NotBlank(message = _LAST_NAME_REQUIRED)
            @Size(max = 100, message = _LAST_NAME_SIZE)
            String lastName,

            @NotBlank(message = _EMAIL_REQUIRED)
            @Email(message = _EMAIL_INVALID)
            String email
    ) implements AppUserRequest {}

    @Schema(name = "AppUserBlock")
    record Block(
            @NotBlank(message = _BLOCKED_REASON_REQUIRED)
            @Size(max = 500, message = _BLOCKED_REASON_SIZE)
            String blockedReason
    ) implements AppUserRequest {}
}

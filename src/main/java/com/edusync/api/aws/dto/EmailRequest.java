package com.edusync.api.aws.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public sealed interface EmailRequest {

    @Schema(name = "EmailSend")
    record Send(
            @NotBlank(message = "Recipient email is required")
            @Email(message = "Recipient email must be valid")
            String to,

            @NotBlank(message = "Subject is required")
            @Size(max = 255, message = "Subject must not exceed 255 characters")
            String subject,

            @NotBlank(message = "Body is required")
            String body,

            boolean html
    ) implements EmailRequest {}

    @Schema(name = "EmailBulkSend")
    record BulkSend(
            @Size(min = 1, max = 50, message = "Must provide between 1 and 50 recipients")
            List<@Email(message = "Each recipient email must be valid") String> to,

            @NotBlank(message = "Subject is required")
            @Size(max = 255, message = "Subject must not exceed 255 characters")
            String subject,

            @NotBlank(message = "Body is required")
            String body,

            boolean html
    ) implements EmailRequest {}
}

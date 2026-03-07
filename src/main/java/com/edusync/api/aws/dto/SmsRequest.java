package com.edusync.api.aws.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public sealed interface SmsRequest {

    @Schema(name = "SmsSend")
    record Send(
            @NotBlank(message = "Phone number is required")
            @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format (e.g. +27821234567)")
            String phoneNumber,

            @NotBlank(message = "Message is required")
            @Size(max = 160, message = "Message must not exceed 160 characters")
            String message
    ) implements SmsRequest {}

    @Schema(name = "SmsBulkSend")
    record BulkSend(
            @NotBlank(message = "Message is required")
            @Size(max = 160, message = "Message must not exceed 160 characters")
            String message,

            @Size(min = 1, max = 100, message = "Must provide between 1 and 100 phone numbers")
            java.util.List<@Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Each phone number must be in E.164 format") String> phoneNumbers
    ) implements SmsRequest {}
}

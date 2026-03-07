package com.edusync.api.aws.dto;

public record SmsResponse(
        String phoneNumber,
        String messageId,
        String status
) {}

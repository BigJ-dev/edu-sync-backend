package com.edusync.api.aws.dto;

public record EmailResponse(
        String to,
        String messageId,
        String status
) {}

package com.edusync.api.aws.dto;

import com.edusync.api.aws.enums.HttpMethod;

import java.time.Instant;

public record PresignedUrlResponse(
        String url,
        String key,
        HttpMethod method,
        Instant expiresAt
) {}

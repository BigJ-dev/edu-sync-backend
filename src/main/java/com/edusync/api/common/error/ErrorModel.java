package com.edusync.api.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorModel(
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors,
        Instant timestamp
) {
    public static ErrorModel of(int status, String error, String message) {
        return new ErrorModel(status, error, message, null, Instant.now());
    }

    public static ErrorModel withFieldErrors(int status, String error, String message, Map<String, String> fieldErrors) {
        return new ErrorModel(status, error, message, fieldErrors, Instant.now());
    }
}

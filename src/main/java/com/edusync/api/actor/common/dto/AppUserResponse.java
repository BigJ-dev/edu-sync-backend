package com.edusync.api.actor.common.dto;

import com.edusync.api.actor.common.model.AppUser;

import java.time.Instant;
import java.util.UUID;

public record AppUserResponse(
        UUID uuid,
        String cognitoSub,
        String email,
        String firstName,
        String lastName,
        boolean active,
        Instant blockedAt,
        String blockedReason,
        Instant lastLoginAt,
        Instant createdAt,
        Instant updatedAt
) {
    public static AppUserResponse from(AppUser user) {
        return new AppUserResponse(
                user.getUuid(),
                user.getCognitoSub(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.isActive(),
                user.getBlockedAt(),
                user.getBlockedReason(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

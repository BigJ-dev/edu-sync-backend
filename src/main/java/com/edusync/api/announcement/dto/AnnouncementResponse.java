package com.edusync.api.announcement.dto;

import com.edusync.api.announcement.enums.AnnouncementCategory;
import com.edusync.api.announcement.enums.AnnouncementStatus;
import com.edusync.api.announcement.model.Announcement;

import java.time.Instant;
import java.util.UUID;

public record AnnouncementResponse(
        UUID uuid,
        String title,
        String body,
        AnnouncementCategory category,
        AnnouncementStatus status,
        boolean pinned,
        UUID publishedByUuid,
        Instant publishedAt,
        Instant expiresAt,
        String attachmentS3Key,
        String attachmentName,
        Instant createdAt,
        Instant updatedAt
) {
    public static AnnouncementResponse from(Announcement a) {
        return new AnnouncementResponse(
                a.getUuid(),
                a.getTitle(),
                a.getBody(),
                a.getCategory(),
                a.getStatus(),
                a.isPinned(),
                a.getPublishedBy().getUuid(),
                a.getPublishedAt(),
                a.getExpiresAt(),
                a.getAttachmentS3Key(),
                a.getAttachmentName(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}

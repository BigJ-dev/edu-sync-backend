package com.edusync.api.announcement.dto;

import com.edusync.api.announcement.enums.AnnouncementCategory;
import com.edusync.api.announcement.enums.AnnouncementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public sealed interface AnnouncementRequest {

    @Schema(name = "AnnouncementCreate")
    record Create(
            @NotBlank(message = "Title is required")
            @Size(max = 255, message = "Title must not exceed 255 characters")
            String title,

            @NotBlank(message = "Body is required")
            String body,

            @NotNull(message = "Category is required")
            AnnouncementCategory category,

            @NotNull(message = "Publisher UUID is required")
            UUID publishedByUuid,

            boolean pinned,

            Instant expiresAt,

            @Size(max = 500, message = "Attachment S3 key must not exceed 500 characters")
            String attachmentS3Key,

            @Size(max = 255, message = "Attachment name must not exceed 255 characters")
            String attachmentName
    ) implements AnnouncementRequest {}

    @Schema(name = "AnnouncementUpdate")
    record Update(
            @Size(max = 255, message = "Title must not exceed 255 characters")
            String title,

            String body,

            AnnouncementCategory category,

            boolean pinned,

            Instant expiresAt,

            @Size(max = 500, message = "Attachment S3 key must not exceed 500 characters")
            String attachmentS3Key,

            @Size(max = 255, message = "Attachment name must not exceed 255 characters")
            String attachmentName
    ) implements AnnouncementRequest {}

    @Schema(name = "AnnouncementFilter")
    record Filter(
            AnnouncementCategory category,
            AnnouncementStatus status,
            Boolean pinned,
            String search
    ) implements AnnouncementRequest {}
}

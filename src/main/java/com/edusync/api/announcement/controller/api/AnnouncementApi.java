package com.edusync.api.announcement.controller.api;

import com.edusync.api.announcement.dto.AnnouncementRequest;
import com.edusync.api.announcement.dto.AnnouncementResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Announcements", description = "Institution-wide announcements and news feed. " +
        "Covers exam schedules, holidays, campus news, maintenance notices, and events visible to all users.")
@RequestMapping("/announcements")
public interface AnnouncementApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create an announcement",
            description = "Creates a new announcement in DRAFT status. Use the publish endpoint to make it visible in the feed."
    )
    @ApiResponse(responseCode = "201", description = "Announcement created successfully")
    @ApiResponse(responseCode = "404", description = "Publisher not found")
    AnnouncementResponse create(@Valid @RequestBody AnnouncementRequest.Create request);

    @PutMapping("/{announcementUuid}")
    @Operation(
            summary = "Update an announcement",
            description = "Updates an existing announcement's title, body, category, pinned status, expiry, or attachment."
    )
    @ApiResponse(responseCode = "200", description = "Announcement updated successfully")
    @ApiResponse(responseCode = "404", description = "Announcement not found")
    AnnouncementResponse update(@PathVariable UUID announcementUuid, @Valid @RequestBody AnnouncementRequest.Update request);

    @PatchMapping("/{announcementUuid}/publish")
    @Operation(
            summary = "Publish an announcement",
            description = "Publishes a draft or archived announcement, making it visible in the news feed. Sets the published timestamp."
    )
    @ApiResponse(responseCode = "200", description = "Announcement published successfully")
    @ApiResponse(responseCode = "404", description = "Announcement not found")
    @ApiResponse(responseCode = "409", description = "Announcement is already published")
    AnnouncementResponse publish(@PathVariable UUID announcementUuid);

    @PatchMapping("/{announcementUuid}/archive")
    @Operation(
            summary = "Archive an announcement",
            description = "Archives an announcement, removing it from the active news feed."
    )
    @ApiResponse(responseCode = "200", description = "Announcement archived successfully")
    @ApiResponse(responseCode = "404", description = "Announcement not found")
    @ApiResponse(responseCode = "409", description = "Announcement is already archived")
    AnnouncementResponse archive(@PathVariable UUID announcementUuid);

    @GetMapping("/{announcementUuid}")
    @Operation(
            summary = "Get announcement by UUID",
            description = "Retrieves a single announcement by its unique identifier."
    )
    @ApiResponse(responseCode = "200", description = "Announcement retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Announcement not found")
    AnnouncementResponse findByUuid(@PathVariable UUID announcementUuid);

    @GetMapping
    @Operation(
            summary = "List all announcements",
            description = "Returns all announcements with optional filtering by category, status, pinned flag, and title search. " +
                    "Results are sorted with pinned items first, then by published date descending."
    )
    @ApiResponse(responseCode = "200", description = "Announcements retrieved successfully")
    List<AnnouncementResponse> findAll(AnnouncementRequest.Filter filter);

    @GetMapping("/feed")
    @Operation(
            summary = "Get published news feed",
            description = "Returns only published, non-expired announcements — the public-facing news feed. " +
                    "Pinned announcements appear first, followed by most recent."
    )
    @ApiResponse(responseCode = "200", description = "Feed retrieved successfully")
    List<AnnouncementResponse> feed();

    @DeleteMapping("/{announcementUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete an announcement",
            description = "Permanently deletes an announcement."
    )
    @ApiResponse(responseCode = "204", description = "Announcement deleted successfully")
    @ApiResponse(responseCode = "404", description = "Announcement not found")
    void delete(@PathVariable UUID announcementUuid);
}

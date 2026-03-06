package com.edusync.api.notification.controller.api;

import com.edusync.api.notification.dto.NotificationRequest;
import com.edusync.api.notification.dto.NotificationResponse;
import com.edusync.api.notification.enums.NotificationType;
import com.edusync.api.notification.enums.RecipientType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Notifications", description = "Endpoints for managing notifications. Notifications inform users about events such as sessions, assessments, materials, and messages.")
@RequestMapping("/notifications")
public interface NotificationApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a notification",
            description = "Creates a new notification for a recipient. Intended for internal/system use."
    )
    @ApiResponse(responseCode = "201", description = "Notification created successfully")
    @ApiResponse(responseCode = "404", description = "Course or module not found")
    NotificationResponse create(@Valid @RequestBody NotificationRequest.Create request);

    @GetMapping
    @Operation(
            summary = "List notifications",
            description = "Returns notifications filtered by recipient, type, course, and read status."
    )
    @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully")
    List<NotificationResponse> findAll(
            @RequestParam(required = false) RecipientType recipientType,
            @RequestParam(required = false) Long recipientId,
            @RequestParam(required = false) NotificationType notificationType,
            @RequestParam(required = false) UUID courseUuid,
            @RequestParam(required = false) Boolean unreadOnly);

    @GetMapping("/count")
    @Operation(
            summary = "Count unread notifications",
            description = "Returns the count of unread notifications for a given recipient."
    )
    @ApiResponse(responseCode = "200", description = "Unread count retrieved successfully")
    long countUnread(
            @RequestParam RecipientType recipientType,
            @RequestParam Long recipientId);

    @GetMapping("/{notificationUuid}")
    @Operation(
            summary = "Get notification by UUID",
            description = "Retrieves a single notification by its unique identifier."
    )
    @ApiResponse(responseCode = "200", description = "Notification retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    NotificationResponse findByUuid(@PathVariable UUID notificationUuid);

    @PatchMapping("/{notificationUuid}/read")
    @Operation(
            summary = "Mark notification as read",
            description = "Sets the read timestamp on a notification."
    )
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    NotificationResponse markAsRead(@PathVariable UUID notificationUuid);

    @PatchMapping("/read-all")
    @Operation(
            summary = "Mark all notifications as read",
            description = "Marks all unread notifications as read for a given recipient."
    )
    @ApiResponse(responseCode = "204", description = "All notifications marked as read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void markAllAsRead(
            @RequestParam RecipientType recipientType,
            @RequestParam Long recipientId);

    @PatchMapping("/{notificationUuid}/dismiss")
    @Operation(
            summary = "Dismiss a notification",
            description = "Sets the dismissed timestamp on a notification."
    )
    @ApiResponse(responseCode = "200", description = "Notification dismissed")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    NotificationResponse dismiss(@PathVariable UUID notificationUuid);
}

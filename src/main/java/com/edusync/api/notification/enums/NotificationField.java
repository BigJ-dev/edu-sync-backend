package com.edusync.api.notification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationField {

    RECIPIENT_TYPE("recipientType"),
    RECIPIENT_ID("recipientId"),
    NOTIFICATION_TYPE("notificationType"),
    COURSE_ID("courseId"),
    READ_AT("readAt"),
    DISMISSED_AT("dismissedAt"),
    CREATED_AT("createdAt");

    private final String name;
}

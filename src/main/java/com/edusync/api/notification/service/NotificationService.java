package com.edusync.api.notification.service;

import com.edusync.api.notification.dto.NotificationRequest;
import com.edusync.api.notification.dto.NotificationResponse;
import com.edusync.api.notification.enums.RecipientType;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest.Create request);

    List<NotificationResponse> findAllNotifications(NotificationRequest.Filter filter);

    NotificationResponse findNotificationByUuid(UUID notificationUuid);

    NotificationResponse markNotificationAsRead(UUID notificationUuid);

    void markAllNotificationsAsRead(RecipientType recipientType, Long recipientId);

    NotificationResponse dismissNotification(UUID notificationUuid);

    long countUnreadNotifications(RecipientType recipientType, Long recipientId);
}

package com.edusync.api.notification.controller;

import com.edusync.api.notification.controller.api.NotificationApi;
import com.edusync.api.notification.dto.NotificationRequest;
import com.edusync.api.notification.dto.NotificationResponse;
import com.edusync.api.notification.enums.RecipientType;
import com.edusync.api.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class NotificationController implements NotificationApi {

    private final NotificationService service;

    @Override
    public NotificationResponse createNotification(NotificationRequest.Create request) {
        return service.createNotification(request);
    }

    @Override
    public List<NotificationResponse> findAllNotifications(NotificationRequest.Filter filter) {
        return service.findAllNotifications(filter);
    }

    @Override
    public long countUnreadNotifications(RecipientType recipientType, Long recipientId) {
        return service.countUnreadNotifications(recipientType, recipientId);
    }

    @Override
    public NotificationResponse findNotificationByUuid(UUID notificationUuid) {
        return service.findNotificationByUuid(notificationUuid);
    }

    @Override
    public NotificationResponse markNotificationAsRead(UUID notificationUuid) {
        return service.markNotificationAsRead(notificationUuid);
    }

    @Override
    public void markAllNotificationsAsRead(RecipientType recipientType, Long recipientId) {
        service.markAllNotificationsAsRead(recipientType, recipientId);
    }

    @Override
    public NotificationResponse dismissNotification(UUID notificationUuid) {
        return service.dismissNotification(notificationUuid);
    }
}

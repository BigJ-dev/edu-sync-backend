package com.edusync.api.notification.controller;

import com.edusync.api.notification.controller.api.NotificationApi;
import com.edusync.api.notification.dto.NotificationRequest;
import com.edusync.api.notification.dto.NotificationResponse;
import com.edusync.api.notification.enums.NotificationType;
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
    public NotificationResponse create(NotificationRequest.Create request) {
        return service.create(request);
    }

    @Override
    public List<NotificationResponse> findAll(RecipientType recipientType, Long recipientId,
                                              NotificationType notificationType, UUID courseUuid,
                                              Boolean unreadOnly) {
        return service.findAll(recipientType, recipientId, notificationType, courseUuid, unreadOnly);
    }

    @Override
    public long countUnread(RecipientType recipientType, Long recipientId) {
        return service.countUnread(recipientType, recipientId);
    }

    @Override
    public NotificationResponse findByUuid(UUID notificationUuid) {
        return service.findByUuid(notificationUuid);
    }

    @Override
    public NotificationResponse markAsRead(UUID notificationUuid) {
        return service.markAsRead(notificationUuid);
    }

    @Override
    public void markAllAsRead(RecipientType recipientType, Long recipientId) {
        service.markAllAsRead(recipientType, recipientId);
    }

    @Override
    public NotificationResponse dismiss(UUID notificationUuid) {
        return service.dismiss(notificationUuid);
    }
}

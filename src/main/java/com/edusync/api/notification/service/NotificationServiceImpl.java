package com.edusync.api.notification.service;

import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.module.repo.ModuleRepository;
import com.edusync.api.notification.dto.NotificationRequest;
import com.edusync.api.notification.dto.NotificationResponse;
import com.edusync.api.notification.enums.RecipientType;
import com.edusync.api.notification.model.Notification;
import com.edusync.api.notification.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public NotificationResponse createNotification(NotificationRequest.Create request) {
        var course = Objects.nonNull(request.courseUuid())
                ? courseRepository.findByUuid(request.courseUuid())
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"))
                : null;

        var module = Objects.nonNull(request.moduleUuid())
                ? moduleRepository.findByUuid(request.moduleUuid())
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"))
                : null;

        var notification = Notification.builder()
                .recipientType(request.recipientType())
                .recipientId(request.recipientId())
                .notificationType(request.notificationType())
                .title(request.title())
                .body(request.body())
                .course(course)
                .module(module)
                .entityType(request.entityType())
                .entityId(request.entityId())
                .build();

        return NotificationResponse.from(repository.save(notification));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> findAllNotifications(NotificationRequest.Filter filter) {
        var courseId = Objects.nonNull(filter.courseUuid())
                ? courseRepository.findByUuid(filter.courseUuid())
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"))
                    .getId()
                : null;

        var spec = Specification.where(NotificationSpec.hasRecipientType(filter.recipientType()))
                .and(NotificationSpec.hasRecipientId(filter.recipientId()))
                .and(NotificationSpec.hasNotificationType(filter.notificationType()))
                .and(NotificationSpec.hasCourseId(courseId));

        if (Boolean.TRUE.equals(filter.unreadOnly())) {
            spec = spec.and(NotificationSpec.isUnread());
        }

        return repository.findAll(spec).stream().map(NotificationResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse findNotificationByUuid(UUID notificationUuid) {
        return NotificationResponse.from(findNotification(notificationUuid));
    }

    @Override
    public NotificationResponse markNotificationAsRead(UUID notificationUuid) {
        var notification = findNotification(notificationUuid);
        notification.setReadAt(Instant.now());
        return NotificationResponse.from(repository.save(notification));
    }

    @Override
    public void markAllNotificationsAsRead(RecipientType recipientType, Long recipientId) {
        var spec = Specification.where(NotificationSpec.hasRecipientType(recipientType))
                .and(NotificationSpec.hasRecipientId(recipientId))
                .and(NotificationSpec.isUnread());

        var notifications = repository.findAll(spec);
        var now = Instant.now();
        notifications.forEach(n -> n.setReadAt(now));
        repository.saveAll(notifications);
    }

    @Override
    public NotificationResponse dismissNotification(UUID notificationUuid) {
        var notification = findNotification(notificationUuid);
        notification.setDismissedAt(Instant.now());
        return NotificationResponse.from(repository.save(notification));
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadNotifications(RecipientType recipientType, Long recipientId) {
        var spec = Specification.where(NotificationSpec.hasRecipientType(recipientType))
                .and(NotificationSpec.hasRecipientId(recipientId))
                .and(NotificationSpec.isUnread());

        return repository.count(spec);
    }

    private Notification findNotification(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Notification was not found"));
    }
}

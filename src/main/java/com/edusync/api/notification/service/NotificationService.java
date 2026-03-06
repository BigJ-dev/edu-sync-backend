package com.edusync.api.notification.service;

import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.module.repo.ModuleRepository;
import com.edusync.api.notification.dto.NotificationRequest;
import com.edusync.api.notification.dto.NotificationResponse;
import com.edusync.api.notification.enums.NotificationType;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository repository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    public NotificationResponse create(NotificationRequest.Create request) {
        Course course = null;
        if (request.courseUuid() != null) {
            course = courseRepository.findByUuid(request.courseUuid())
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
        }

        CourseModule module = null;
        if (request.moduleUuid() != null) {
            module = moduleRepository.findByUuid(request.moduleUuid())
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"));
        }

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

    @Transactional(readOnly = true)
    public List<NotificationResponse> findAll(RecipientType recipientType, Long recipientId,
                                              NotificationType notificationType, UUID courseUuid,
                                              Boolean unreadOnly) {
        Long courseId = null;
        if (courseUuid != null) {
            var course = courseRepository.findByUuid(courseUuid)
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
            courseId = course.getId();
        }

        var spec = Specification.where(NotificationSpec.hasRecipientType(recipientType))
                .and(NotificationSpec.hasRecipientId(recipientId))
                .and(NotificationSpec.hasNotificationType(notificationType))
                .and(NotificationSpec.hasCourseId(courseId));

        if (Boolean.TRUE.equals(unreadOnly)) {
            spec = spec.and(NotificationSpec.isUnread());
        }

        return repository.findAll(spec).stream().map(NotificationResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public NotificationResponse findByUuid(UUID notificationUuid) {
        return NotificationResponse.from(findNotification(notificationUuid));
    }

    public NotificationResponse markAsRead(UUID notificationUuid) {
        var notification = findNotification(notificationUuid);
        notification.setReadAt(Instant.now());
        return NotificationResponse.from(repository.save(notification));
    }

    public void markAllAsRead(RecipientType recipientType, Long recipientId) {
        var spec = Specification.where(NotificationSpec.hasRecipientType(recipientType))
                .and(NotificationSpec.hasRecipientId(recipientId))
                .and(NotificationSpec.isUnread());

        var notifications = repository.findAll(spec);
        var now = Instant.now();
        notifications.forEach(n -> n.setReadAt(now));
        repository.saveAll(notifications);
    }

    public NotificationResponse dismiss(UUID notificationUuid) {
        var notification = findNotification(notificationUuid);
        notification.setDismissedAt(Instant.now());
        return NotificationResponse.from(repository.save(notification));
    }

    @Transactional(readOnly = true)
    public long countUnread(RecipientType recipientType, Long recipientId) {
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

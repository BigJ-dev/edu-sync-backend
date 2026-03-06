package com.edusync.api.notification.service;

import com.edusync.api.notification.enums.NotificationType;
import com.edusync.api.notification.enums.RecipientType;
import com.edusync.api.notification.model.Notification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.edusync.api.notification.enums.NotificationField.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotificationSpec {

    public static Specification<Notification> hasRecipientType(RecipientType recipientType) {
        return recipientType == null ? null : (root, query, cb) -> cb.equal(root.get(RECIPIENT_TYPE.getName()), recipientType);
    }

    public static Specification<Notification> hasRecipientId(Long recipientId) {
        return recipientId == null ? null : (root, query, cb) -> cb.equal(root.get(RECIPIENT_ID.getName()), recipientId);
    }

    public static Specification<Notification> hasNotificationType(NotificationType notificationType) {
        return notificationType == null ? null : (root, query, cb) -> cb.equal(root.get(NOTIFICATION_TYPE.getName()), notificationType);
    }

    public static Specification<Notification> hasCourseId(Long courseId) {
        return courseId == null ? null : (root, query, cb) -> cb.equal(root.get("course").get("id"), courseId);
    }

    public static Specification<Notification> isUnread() {
        return (root, query, cb) -> cb.isNull(root.get(READ_AT.getName()));
    }

    public static Specification<Notification> isUndismissed() {
        return (root, query, cb) -> cb.isNull(root.get(DISMISSED_AT.getName()));
    }
}

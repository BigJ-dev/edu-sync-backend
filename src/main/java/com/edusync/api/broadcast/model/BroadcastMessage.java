package com.edusync.api.broadcast.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.broadcast.enums.BroadcastPriority;
import com.edusync.api.broadcast.enums.BroadcastTarget;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.module.model.CourseModule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "broadcast_message", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BroadcastMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private CourseModule module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_by", nullable = false)
    private AppUser sentBy;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "broadcast_target")
    private BroadcastTarget targetType;

    private Long targetSessionId;

    @Column(length = 500)
    private String attachmentS3Key;

    @Column(length = 255)
    private String attachmentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "broadcast_priority")
    private BroadcastPriority priority;

    @Column(nullable = false)
    private boolean sendEmail;

    private Instant sentAt;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @PrePersist
    void generateUuid() {
        if (uuid == null) uuid = UUID.randomUUID();
    }
}

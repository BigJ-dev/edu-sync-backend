package com.edusync.api.course.session.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import com.edusync.api.course.module.model.CourseModule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "class_session", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false)
    private AppUser lecturer;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "session_type")
    private ClassSessionType sessionType;

    @Column(nullable = false)
    private int sessionNumber;

    @Column(nullable = false)
    private Instant scheduledStart;

    @Column(nullable = false)
    private Instant scheduledEnd;

    private Instant actualStart;

    private Instant actualEnd;

    @Column(length = 512)
    private String teamsMeetingId;

    private String teamsJoinUrl;

    @Column(length = 500)
    private String recordingS3Key;

    @Column(length = 500)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "session_status")
    private ClassSessionStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    void generateUuid() {
        if (uuid == null) uuid = UUID.randomUUID();
    }
}

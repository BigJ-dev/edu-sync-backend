package com.edusync.api.announcement.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.announcement.enums.AnnouncementCategory;
import com.edusync.api.announcement.enums.AnnouncementStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "announcement", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "announcement_category")
    private AnnouncementCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "announcement_status")
    private AnnouncementStatus status;

    @Column(nullable = false)
    private boolean pinned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_by", nullable = false)
    private AppUser publishedBy;

    private Instant publishedAt;

    private Instant expiresAt;

    @Column(length = 500)
    private String attachmentS3Key;

    @Column(length = 255)
    private String attachmentName;

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

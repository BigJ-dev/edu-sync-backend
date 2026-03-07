package com.edusync.api.actor.student.model;

import com.edusync.api.actor.common.model.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "student", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @Column(unique = true)
    private String cognitoSub;

    @Column(unique = true, nullable = false, length = 50)
    private String studentNumber;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String azureAdGuestId;

    @Column(length = 500)
    private String profileImageS3Key;

    @Column(nullable = false)
    private boolean active = true;

    private Instant blockedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_by")
    private AppUser blockedBy;

    @Column(length = 500)
    private String blockedReason;

    private Instant lastLoginAt;

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

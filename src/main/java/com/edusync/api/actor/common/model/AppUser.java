package com.edusync.api.actor.common.model;

import com.edusync.api.actor.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "app_user", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @Column(unique = true, nullable = false)
    private String cognitoSub;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "user_role")
    private UserRole role;

    @Column(nullable = false)
    private boolean active = true;

    private Instant blockedAt;

    private Long blockedBy;

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

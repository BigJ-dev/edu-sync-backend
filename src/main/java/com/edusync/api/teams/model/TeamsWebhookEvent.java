package com.edusync.api.teams.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "teams_webhook_event", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamsWebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String eventId;

    private String resourceUrl;

    @Column(length = 50)
    private String changeType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String payload;

    @Column(nullable = false)
    private boolean processed;

    private Instant processedAt;

    private String errorMessage;

    @Column(nullable = false)
    private int retryCount;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;
}

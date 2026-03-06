package com.edusync.api.teams.model;

import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.teams.enums.ReportSyncStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "teams_attendance_report", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamsAttendanceReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_session_id", nullable = false)
    private ClassSession classSession;

    @Column(nullable = false, length = 512)
    private String graphReportId;

    private Instant meetingStart;

    private Instant meetingEnd;

    private Integer totalParticipantCount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private String rawJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "report_sync_status")
    private ReportSyncStatus syncStatus;

    private Instant syncedAt;

    private String errorMessage;

    @Column(nullable = false)
    private int retryCount;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    void generateUuid() {
        if (uuid == null) uuid = UUID.randomUUID();
        if (syncStatus == null) syncStatus = ReportSyncStatus.PENDING;
    }
}
